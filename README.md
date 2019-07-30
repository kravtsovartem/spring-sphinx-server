# spring-sphinx-server

Сервер на Java с использованием фреймворка Spring с подключенным решением SphinxAPI.

На текущий момент реализовано 3 endpoints:
- /search - поиск
- /search/suggest - подсказки
- /item - документ

## Примеры. Структура запроса и ответа.

### /search - поиск
```
import { ajax } from 'rxjs/ajax'
ajax({
	url: '/search',
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
	},
	body: JSON.stringify({ 
		value: "Поисковое слово", 
		page: "Текущая страница", 
		limit: "Кол-во выводимых документов"
	}),
})
```
```
// Стандартный ответ от Sphinx.
data: {
	attrNames: ["uid", "name", "text"]
	attrTypes: [7, 7, 7]
	error: null
	fields: ["uid", "name", "text"]
	matches: [
		0: {
			attrValues: [
				0: "123456789",
				1: "Название документа",
				2: "Текст документа",
				3: "Часть текста документа с <strong>выделенными</strong> вхождениями поискового слова"
			],
			docId: 123456789,
			weight: 2778
		}
	],
	status: 0
	time: 0.001
	total: 2
	totalFound: 2
	warning: null
	words: [{word: "array*", docs: 2, hits: 3}, {word: "=arrayselect", docs: 2, hits: 3},…]
}
```

### /search/suggest - подсказки
```
import { ajax } from 'rxjs/ajax'
ajax({
	url: '/search/suggest',
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
	},
	body: JSON.stringify({ 
		value: "Поисковое слово",
		limit: "Кол-во выводимых подсказок"
	}),
})
```
```
// Подсказки выделяются сразу по вхождению слова.
data: [
	0: "<strong>ArrayCount</strong>",
	1: "Array"
]
```

### /item - документ
```
import { ajax } from 'rxjs/ajax'
ajax({
	url: '/search/suggest',
	method: 'POST',
	headers: {
		'Content-Type': 'application/json',
	},
	body: JSON.stringify({ 
		value: "uid документа"
	}),
})
```
```
data: {
	uid: "id документа",
	name: "Название",
	text: "Текст документа"
}
```
