Insert into shopping_list (id)
VALUES (1),
       (2),
       (3);

Insert into user (id, email, name, shopping_list_fk)
VALUES (1, 'test1@test.com', 'User 1', 1),
       (2, 'test2@test.com', 'User 2', 2),
       (3, 'test3@test.com', 'User 3', 3);

Insert into product (id, name, best_before_time, price)
VALUES (1, 'Apfel', 5, 4),
       (2, 'Banane', 4, 3),
       (3, 'Karotte', 5, 6);

insert into shopping_list_product (product_id, shopping_list_id, amount)
VALUES (1, 1, 2),
       (2, 1, 6),
       (1, 2, 3);