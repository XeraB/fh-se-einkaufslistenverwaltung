Insert into shopping_list (id, invite_code, version)
VALUES (1, 'test1', 0),
       (2, 'test2', 0),
       (3, 'test3', 0),
       (4, 'test4', 0);

Insert into user (id, email, name, shopping_list_fk, role, version)
VALUES (1, 'test1@test.com', 'User 1', 1, 1, 0),
       (2, 'test2@test.com', 'User 2', 2, 1, 0),
       (3, 'test3@test.com', 'User 3', 3, 1, 0),
       (4, 'test4@test.com', 'User 4', 4, 1, 0);

Insert into product (id, name, best_before_time, price, version)
VALUES (1, 'Apfel', 5, 4, 0),
       (2, 'Banane', 4, 3, 0),
       (3, 'Karotte', 5, 6, 0),
       (4, 'Brot', 4, 3, 0);

insert into shopping_list_product (product_id, shopping_list_id, amount, unit, version)
VALUES (1, 1, 2, 0, 0),
       (2, 1, 6, 0, 0),
       (1, 2, 3, 0, 0),
       (4, 1, 3, 0, 0);