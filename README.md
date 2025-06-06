# 🎬 Filmorate — Ваш персональный гид в мире кино

![Filmorate Banner](image_filmorate.jpeg)

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.2.4-green)
![JDBC](https://img.shields.io/badge/JDBC-3.1-orange)
![Lombok](https://img.shields.io/badge/Lombok-1.18.34-red)
![JUnit](https://img.shields.io/badge/JUnit-5-orange)
![Maven](https://img.shields.io/badge/Maven-4.0.0-blue)
![H2](https://img.shields.io/badge/H2-2.3-blue)

---

## 📝 Описание проекта

**Filmorate** — это веб-приложение для киноманов, которое объединяет функции кинообозревателя и социальной платформы. Мы создали этот проект, чтобы решить проблему поиска фильмов, общения с единомышленниками и получения персонализированных рекомендаций в одном удобном месте. Это не просто каталог фильмов — это сообщество, где вы можете делиться впечатлениями, находить друзей и вдохновляться на новые просмотры.

**Зачем мы это делали?**  
Изначально Filmorate родился как учебный проект для отработки навыков работы с **Spring Boot** и **JDBC**, но превратился в нечто большее. Нам хотелось создать инструмент, который:
- Помогает пользователям находить фильмы по своим вкусам.
- Дает возможность обсуждать кино с друзьями через лайки, отзывы и рекомендации.
- Объединяет любителей кино в одном месте.

**Какую боль он закрывает?**  
Если вы устали от бесконечного скроллинга стриминговых платформ или хотите узнать, что смотрят ваши друзья, Filmorate — это ваше решение. Здесь вы найдете не только фильмы, но и сообщество, которое разделяет вашу страсть к кино.

**Технологический стек:**
- **Java 21** — основа нашего надежного кода.
- **Spring Boot 3.2.4** — для быстрого старта и удобного управления.
- **JDBC** — для работы с базой данных.
- **Lombok** — чтобы писать меньше бойлерплейта.
- **JUnit 5** — для тестирования функциональности.
- **Maven** — для сборки и управления зависимостями.
- **H2 Database** — легковесная база данных для хранения данных в памяти.

---

## 🚀 Возможности

Filmorate предлагает всё, что нужно любителю кино:
- **Управление данными:**
  - Создание, обновление и удаление фильмов, пользователей и режиссёров.
- **Хранение:** Данные хранятся в памяти с использованием H2.
- **Поиск:** Фильтры по названию и режиссёрам.
- **Социальные функции:**
  - Ставьте **лайки** фильмам.
  - Оставляйте **отзывы**.
  - Добавляйте друзей (двухсторонняя дружба).
  - Просматривайте **общих друзей**.
- **Рекомендации:** Персонализированные подборки на основе ваших предпочтений.
- **Активность:**
  - Рейтинг фильмов.
  - Лента событий друзей.
  - Список самых активных пользователей.
- **Эндпоинты API:**
  - `/users` — управление пользователями.
  - `/films` — работа с фильмами.
  - `/genres` — жанры.
  - `/mpa` — рейтинги MPA.
  - `/reviews` — отзывы.
  - `/directors` — режиссёры.

---

## 🛠️ Инструкция по развёртыванию

### Системные требования
- **Java 21** или выше.
- **Maven 4.0.0** или выше.
- **Операционная система:** Windows, macOS, Linux.
- **H2 Database** (встроена, дополнительные настройки не требуются).

### Шаги для запуска
1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/your-username/filmorate.git

2. **Перейдите в директорию проекта:**
   ```bash
   cd filmorate

3. **Соберите проект:**
   ```bash
   mvn clean install

4. **Запустите приложение:**
  - В среде разработки (IntelliJ IDEA, Eclipse, VSCode): 
    найдите класс **FilmorateApplication.java** и нажмите "Run".
  - Или через терминал: **mvn spring-boot:run**

5. **Проверьте работу:**
   Откройте браузер и перейдите по адресу **http://localhost:8080**

Если всё сделано правильно, вы увидите работающее приложение!

## 📋 Планы по доработке
 Мы не собираемся останавливаться! Вот что мы планируем:
 - Оптимизация поиска: Добавить фильтры по жанрам и году выпуска через новые индексы в H2 для ускорения запросов.
 - Улучшение рекомендаций: Интегрировать простую систему машинного обучения с использованием библиотеки Apache Mahout для анализа предпочтений.
 - Интерфейс: Переписать фронтенд на React, чтобы сделать интерфейс более интерактивным и современным.
 - Расширение API: Добавить эндпоинт /films/popular с сортировкой по количеству лайков и просмотров.
 - Тестирование: Покрыть сложные сценарии интеграционными тестами с помощью Testcontainers.

## 🌟 Почему стоит попробовать Filmorate?
 - Простота: Всё, что нужно для кино — в одном месте.
 - Социальность: Делитесь впечатлениями и находите друзей.
 - Персонализация: Рекомендации, которые действительно работают для вас.

## 📬 Связаться с нами
 - Email: yasha.golotin@mail.ru
