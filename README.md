# java-filmorate
Template repository for Filmorate project.

## ER-диаграмма

![filmorate](ER-диаграмма базы данных filmorate.png)

Пример запроса:
Получение топ-10 названий фильмов по количеству лайков:

```sql
SELECT name
FROM film
WHERE film_id IN (SELECT film_id
                  FROM like
                  GROUP BY film_id
                  ORDER BY COUNT(user_id) desc
                   LIMIT 10);
```

Получение id и логинов друзей по id пользователя = 1:

```sql
SELECT u.login,
       u.user_id
FROM user AS u
WHERE u.user_id IN (SELECT f.user_second_id
                    FROM friendship AS f
                    WHERE f.user_first_id = 1);
```


