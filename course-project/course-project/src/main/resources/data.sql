
insert into courses (course_name, username, course_status)
values
('Developia', 'admin',1),
('Yeni kurs', 'yeni', 1);


 insert into admins
 (name,surname,username,creator_username,course_id)
 values
 ('Jamil','Mammadov','admin','author',1),
 ('Ferhad','Kerimov','yeni','author',2);
 

insert into users
(username,password,enabled,type,course_id)
values
 ('author','{bcrypt}$2a$12$Wp6W/qqPO5TXCZ4gkSgK..V.OC8GrV3ULifQG2sDO.LdKBnVBFzli',1,'Author',0),
 ('admin','{bcrypt}$2a$12$nihb5JgXiSHQFSdj39Doeu4SK2gVMViJxWP.1sdfuLJRv/EKY7lBK',1,'Admin',1),
 ('yeni','{bcrypt}$2a$12$/ppvyHs5niN5CQuZtCL5bOxIgtFQZ8I.vL/JyKp2/zEjyzdrjweJK',1,'Admin',2),
 ('man2','{bcrypt}$2a$12$XeWuAHYL7OQHsS0uLRGMp.msrZKD3U/p83JGgVstQVHvBJsiFnQEe',1,'Manager',2),
 ('manager','{bcrypt}$2a$12$U9DLlcayihjsfJC5cw6UVO3qJzMsEYhjDRsa2rVGJAlpuQhPHj8MG',1,'Manager',1),
 ('kerim1','{bcrypt}$2a$12$2qE.OjMi83IAbaDjf4ldZ.os8J4aMjY.mtfKyXc8ZowYfub046pL6',1,'Teacher',1),
 ('ilqar1','{bcrypt}$2a$12$lBWopZuamFj5EyxE4OhlZOl0FP6v7NYxfDD8aJK1Wzu7F2bttBgQm',1,'Teacher',2),
 ('aydin1','{bcrypt}$2a$12$/gp1qh2/pepSoIR0O9mQyOst5rrbR0MG4kSPzac2MZHLgn6nKLZF.',1,'Student',1),
 ('aysel1','{bcrypt}$2a$12$esZcOvL6bTFchXfqoPIuEuuY2bwAOSZvlzYEaaDR9YvBk.TNIjDxq',1,'Student',1),
 ('hilal','{bcrypt}$2a$12$3jZlmoHUYzrYjYdVThcxa.uMLUzrgAE8.G/sM4TA.3CyRfpDYi5zW',1,'Student',2),
 ('ruslan1','{bcrypt}$2a$12$y6NfyyZ1RS16zDcnjBrj9.eZkedI1eRms87/wy.4Yk2XqL10TGEIm',1,'Student',2);
 
 insert into managers
 (name,surname,creator_username,course_id,username)
 values
 ('Zahid','Xelilov','admin',1,'manager'),
 ('Novruz','Babasov','yeni',2,'man2');
 
 

insert into
 authorities_list
(authority,author,admin,manager,teacher,parent,student)
values
 ('author',1, 0, 0, 0, 0, 0),
 ('admin',0, 1, 0, 0, 0, 0),
 ('manager',0, 0, 1, 0, 0, 0),
 ('teacher',0, 0, 0, 1, 0, 0),
 ('parent',0, 0, 0, 0, 1, 0),
 ('student',0, 0, 0, 0, 0, 1),
 
 ('find:all:courses',1, 0, 0, 0, 0, 0),
 ('created:course',1, 0, 0, 0, 0, 0),
 ('edit:course',1, 0, 0, 0, 0, 0),
 ('find:by:id:course',1, 0, 0, 0, 0, 0),
 ('delete:course',1, 0, 0, 0, 0, 0),
 ('find:all:user',1, 0, 0, 0, 0, 0),
 ('add:user',1, 0, 0, 0, 0, 0),
 ('add:admin',0, 1, 0, 0, 0, 0),
 ('delete:admin',0, 1, 0, 0, 0, 0),
 ('find:all:admin',0, 1, 0, 0, 0, 0),
 ('find:by:id:admin',0, 1, 0, 0, 0, 0),
 ('edit:admin',0, 1, 0, 0, 0, 0),
 ('add:manager',0, 1, 0, 0, 0, 0),
 ('find:all:managers',0, 1, 0, 0, 0, 0),
 ('find:by:id:manager',0, 1, 0, 0, 0, 0),
 ('edit:manager',0, 1, 0, 0, 0, 0),
 ('delete:manager',0, 1, 0, 0, 0, 0),
 ('add:teacher',0, 0, 1, 0, 0, 0),
 ('find:all:teachers',0, 0, 1, 0, 0, 0),
 ('edit:teacher',0, 0, 1, 0, 0, 0),
 ('find:by:id:teacher',0, 0, 1, 0, 0, 0),
 ('delete:teacher',0, 0, 1, 0, 0, 0),
 ('add:student',0, 0, 1, 0, 0, 0),
 ('edit:student',0, 0, 1, 0, 0, 0),
 ('find:all:students',0, 0, 1, 0, 0, 0),
 ('find:by:id:student',0, 0, 1, 0, 0, 0),
 ('delete:student',0, 0, 1, 0, 0, 0),
 ('add:student:note',0, 0, 1, 1, 0, 0),
 ('search:student',0, 0, 1, 0, 0, 0),
 ('find:all:notes:by:studentId',0, 0, 1, 0, 0, 0),
 ('find:all:student:notes',0, 0, 1, 0, 0, 0),
 ('add:parent',0, 0, 1, 0, 0, 0),
 ('find:all:parents',0, 0, 1, 0, 0, 0),
 ('find:by:id:parent',0, 0, 1, 0, 0, 0),
 ('edit:parent',0, 0, 1, 0, 0, 0), 
 ('delete:parent',0, 0, 1, 0, 0, 0),
 ('save:category',0, 0, 1, 0, 0, 0),
 ('find:all:category',0, 0, 1, 0, 0, 0),
 ('find:by:id:category',0, 0, 1, 0, 0, 0),
 ('find:by:id:training',0, 0, 1, 0, 0, 0),
 ('add:training',0, 0, 1, 0, 0, 0),
 ('find:all:trainings',0, 0, 1, 0, 0, 0),
 ('find:by:categoryId:traning',0, 0, 1, 0, 0, 0),
 ('edit:training',0, 0, 1, 0, 0, 0),
 ('delete:training',0, 0, 1, 0, 0, 0),
 ('find:all:contract:by:studentId',0, 0, 1, 0, 0, 0),
 ('find:all:groups',0, 0, 0, 1, 0, 0),
 ('find:by:id:group',0, 0, 0, 1, 0, 0),
 ('created:group',0, 0, 0, 1, 0, 0),
 ('edit:group',0, 0, 0, 1, 0, 0),
 ('delete:group',0, 0, 0, 1, 0, 0),
 ('save:contract',0, 0, 0, 1, 0, 0),
 ('delete:contract:by:id',0, 0, 0, 1, 0, 0),
 ('find:all:contract:by:groupId',0, 0, 0, 1, 0, 0);
 
 
 
 
 

insert into authorities
(username, authority)
select 'author' , authority from `authorities_list` where author=1;

insert into authorities
(username, authority)
select 'admin' , authority from `authorities_list` where admin=1;

insert into authorities
(username, authority)
select 'yeni' , authority from `authorities_list` where admin=1;

insert into authorities
(username, authority)
select 'manager' , authority from `authorities_list` where manager=1;

insert into authorities
(username, authority)
select 'man2' , authority from `authorities_list` where manager=1;


insert into authorities
(username, authority)
select 'kerim1' , authority from `authorities_list` where teacher=1;

insert into authorities
(username, authority)
select 'ilqar1' , authority from `authorities_list` where teacher=1;


insert into teachers
(name,surname,username,course_id,creator_username,manager_id)
values
('Kerim','Movsumov','kerim1',1,'manager',1),
('Ilqar','Memmedov','ilqar1',2,'man2',2);

  
insert into students
(name,surname,username,manager_id,course_id,creator_username)
values
('Aydin','Memmedov','aydin1',1,1,'manager'),
('Aydin','Ilyasova','aysel1',1,1,'manager'),
('Hilal','Axundov','hilal',2,2,'man2'),
('Ruslan','Mahmudov','ruslan1',2,2,'man2');

insert into student_note
(creator_username,note,now_date,student_id)
values
('manager','yaxsidi','2022-01-01',1),
('manager','pisdi','2022-01-01',1);

insert into contracts
(group_id,student_id)
values
(1,1),
(1,2);



 
 insert into course_groups
 (name,week_day,time,category_id,training_id,course_id,teacher_id)
 values
 ('java6','3-6','19:00',1,1,1,1),
 ('python5','2-4','19:00',1,2,1,1),
 ('java7','1-5','15:00',2,3,2,2),
 ('python5','2-4','19:00',2,4,2,2);
 
 
 insert into categorys
 (name, course_id)
 values
 ('Programming',1),
 ('Programming',2),
 ('Graphic design',1);
 
  insert into trainings
 (name,price,period,course_id,category_id)
 values
 ('Java',200,'6 ay',1,1),
 ('Python',200,'6 ay',1,1),
 ('Java',200,'6 ay',2,1),
 ('Python',200,'6 ay',2,1),
 ('AutoCad',150,'3 ay',1,2),
 ('3DMax',150,'3 ay',1,2);
 
 
 


	