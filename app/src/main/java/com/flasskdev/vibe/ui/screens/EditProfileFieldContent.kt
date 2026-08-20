package com.flasskdev.vibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay
import androidx.activity.compose.BackHandler

@Composable
fun EditProfileFieldContent(
    title: String,
    initialValue: String,
    description: String,
    maxLength: Int,
    icon: ImageVector? = null,
    errorMessage: String? = null,
    successMessage: String? = null,
    filter: ((String) -> String)? = null,
    onValueChange: ((String) -> Unit)? = null,
    onSave: (String) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var textValue by remember(initialValue) { mutableStateOf(initialValue) }
    var showDialog by remember { mutableStateOf(false) }
    val hasChanges = textValue != initialValue
    
    LaunchedEffect(textValue) {
        if (hasChanges) {
            delay(500)
            onValueChange?.invoke(textValue)
        }
    }
    
    BackHandler(enabled = hasChanges) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Сохранить изменения?", fontWeight = FontWeight.Bold) },
            text = { Text("У вас есть несохраненные изменения. Вы хотите их сохранить?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onSave(textValue)
                }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    onBack()
                }) {
                    Text("Сбросить", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, bottom = 80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp).fillMaxWidth()
        ) {
            IconButton(onClick = { 
                if (hasChanges) showDialog = true else onBack() 
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1f)
            )
            
            if (hasChanges) {
                IconButton(onClick = { onSave(textValue) }) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Сохранить",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                    BasicTextField(
                        value = textValue,
                        onValueChange = { 
                            val noNewlines = it.replace("\n", "")
                            val filteredValue = filter?.invoke(noNewlines) ?: noNewlines
                            if (filteredValue.length <= maxLength) {
                                textValue = filteredValue
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
                        decorationBox = { innerTextField ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (icon != null) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    if (textValue.isEmpty()) {
                                        Text(
                                            text = "Введите ${title.lowercase()}", 
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f), 
                                            fontSize = 17.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )
                    
                    val remaining = maxLength - textValue.length
                    Text(
                        text = remaining.toString(),
                        color = if (remaining == 0) Color(0xFFFF3B30) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF3B30),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
            } else if (successMessage != null) {
                Text(
                    text = successMessage,
                    color = Color(0xFF34C759),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
            }
            
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
