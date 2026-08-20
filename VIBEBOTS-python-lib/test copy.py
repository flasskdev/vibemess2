from __future__ import annotations

import logging
import re
from collections import defaultdict, deque
from dataclasses import dataclass, field
from datetime import datetime, timezone
from typing import Deque

import config
from mistralai.client import Mistral
from python.main import Bot, InlineKeyboardButton, InlineKeyboardMarkup


bot = Bot(token=config.token)
mistral = Mistral(api_key=config.mistralai_token)


@bot.command("start")
def start(message):
    bot.reply(message.sender_id, message.message_id, text="Hello!")



if __name__ == "__main__":
    bot.start_polling(skip_updates=True)
