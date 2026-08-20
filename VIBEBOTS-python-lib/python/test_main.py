from __future__ import annotations

import unittest
from typing import Any, Dict

from main import Bot, BotAPIError, DotDict, InlineKeyboardButton, InlineKeyboardMarkup


class FakeResponse:
    def __init__(self, payload: Dict[str, Any]) -> None:
        self.payload = payload

    def raise_for_status(self) -> None:
        return None

    def json(self) -> Dict[str, Any]:
        return self.payload


class FakeSession:
    def __init__(self) -> None:
        self.requests: list[Dict[str, Any]] = []

    def post(self, url: str, json: Dict[str, Any], timeout: float) -> FakeResponse:
        self.requests.append({"url": url, "json": json, "timeout": timeout})
        return FakeResponse({"ok": True, "result": {"id": 100, **json}})


class VibeBotsTests(unittest.TestCase):
    def setUp(self) -> None:
        self.session = FakeSession()
        self.bot = Bot("0123456789abcdef", api_url="https://api.example/v1/", session=self.session)

    def test_keyboard_builders_and_copy(self) -> None:
        keyboard = InlineKeyboardMarkup(row_width=2).add(
            InlineKeyboardButton.callback("One", "one"),
            InlineKeyboardButton.callback("Two", "two"),
            InlineKeyboardButton.link("Docs", "https://example.com/docs"),
        )
        self.assertEqual(len(keyboard), 2)
        self.assertEqual(keyboard.button_count, 3)
        self.assertEqual(keyboard.to_dict()["inline_keyboard"][0][0]["callback_data"], "one")
        self.assertEqual(keyboard.copy().to_dict(), keyboard.to_dict())
        self.assertTrue(InlineKeyboardButton.callback("Again", "again").is_callback)
        self.assertTrue(InlineKeyboardButton.link("Site", "https://example.com").is_url)

    def test_reply_and_action_shortcuts(self) -> None:
        chat = DotDict.from_dict({"chat_id": 17})
        source_message = DotDict.from_dict({"message_id": 42})
        self.bot.reply(chat, source_message, "Hello")
        self.bot.send_typing(17)
        self.bot.send_upload_video(17)
        self.assertEqual(
            self.session.requests[0]["json"],
            {"user_id": 17, "text": "Hello", "reply_to_id": 42},
        )
        self.assertEqual(self.session.requests[1]["json"]["action"], "typing")
        self.assertEqual(self.session.requests[2]["json"]["action"], "upload_video")
        with self.assertRaises(ValueError):
            self.bot.send_chat_action(17, "invalid")

    def test_filtered_handler_dispatch_and_callback_helpers(self) -> None:
        received: list[str] = []

        @self.bot.message_handler(commands="start")
        def start(message: DotDict) -> None:
            received.append(f"message:{message.content}")

        @self.bot.callback_query_handler(data_startswith="diag:")
        def diagnostic(callback) -> None:
            callback.answer("Accepted")
            callback.reply("Next question")
            received.append(f"callback:{callback.data}")

        self.assertEqual(self.bot.dispatch_update({"id": 1, "content": "/start now", "sender_id": 9}), 1)
        self.assertEqual(self.bot.dispatch_update({"id": 2, "content": "hello", "sender_id": 9}), 0)
        self.assertEqual(
            self.bot.dispatch_update({"id": 3, "type": "callback_query", "callback_query_id": 3, "sender_id": 9, "data": "diag:begin"}),
            1,
        )
        self.assertEqual(received, ["message:/start now", "callback:diag:begin"])
        self.assertEqual(self.session.requests[-2]["json"]["callback_query_id"], 3)
        self.assertEqual(self.session.requests[-1]["json"]["user_id"], 9)

    def test_poll_once_calculates_offset_and_dispatches(self) -> None:
        dispatched: list[int] = []
        requested_offsets: list[int | None] = []

        def fake_get_updates(offset=None, skip_updates=False):
            requested_offsets.append(offset)
            if offset == 1:
                return {
                    "ok": True,
                    "result": [{"id": 5, "sender_id": 3, "content": "first"}],
                    "new_offset": 4,
                }
            return {
                "ok": True,
                "result": [{"id": 6, "sender_id": 3, "content": "second"}],
                "new_offset": 5,
            }

        self.bot.get_updates = fake_get_updates
        self.bot.message_handler(lambda message: dispatched.append(message.id))
        updates, offset = self.bot.poll_once(offset=1)
        next_updates, next_offset = self.bot.poll_once(offset=offset)

        self.assertEqual(updates[0].content, "first")
        self.assertEqual(next_updates[0].content, "second")
        self.assertEqual(offset, 5)
        self.assertEqual(next_offset, 6)
        self.assertEqual(requested_offsets, [1, 5])
        self.assertEqual(dispatched, [5, 6])

    def test_dot_dict_paths_and_validation(self) -> None:
        value = DotDict.from_dict({"sender": {"profile": {"name": "Vibe"}}})
        self.assertEqual(value.get_path("sender.profile.name"), "Vibe")
        self.assertIsNone(value.get_path("sender.profile.id"))
        with self.assertRaises(ValueError):
            InlineKeyboardButton("Broken", callback_data="a", url="https://example.com")
        with self.assertRaises(BotAPIError):
            raise BotAPIError("expected")


if __name__ == "__main__":
    unittest.main(verbosity=2)
