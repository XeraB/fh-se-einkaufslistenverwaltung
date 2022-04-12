Insert into shopping_list (id, name)
VALUES (1, 'first'),
       (2, 'second'),
       (3, 'third');

Insert into product (id, name, best_before_time, price)
VALUES (1, 'Apfel', 5, 4),
       (2, 'Banane', 4, 3),
       (3, 'Karotte', 5, 6);

insert into shopping_list_product (product_id, shopping_list_id, amount)
VALUES (1, 1, 2),
       (2, 1, 6),
       (1, 2, 3);