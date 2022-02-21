-- pos_users.blacklist definition

CREATE TABLE `blacklist` (
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- pos_users.`user` definition

CREATE TABLE `user` (
  `client_id` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`client_id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLAT

INSERT INTO pos_users.blacklist (token) VALUES
	 ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY0Mzk4NzI5OSwiZXhwIjoxNjQ0MDA1Mjk5fQ.NK4R_5Abyhb3wrU6UCqyvJuC7mUhMVSstA93NIgnc8I'),
	 ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY0NDAwNzA2MywiZXhwIjoxNjQ0MDI1MDYzfQ.dHUkyuoSjs5c7xyjEIcAniaejVAbC2DforFuN8TIkYk'),
	 ('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGllbnQiLCJpYXQiOjE2NDM5ODU2OTMsImV4cCI6MTY0NDAwMzY5M30.JjR3GcHD44InYBTFFpY1UAbuL0LtSPdMRRw2qqlqKzc');
	 
INSERT INTO pos_users.`user` (client_id,password,rol,username) VALUES
	 ('8a9383827ec4e745017ec4e994d40000','$2a$10$uKDFEdOb7u6YrMJHWIHMpOsZJVFs.75tX7pY1BoayeqMmmjvh7pDi','administrator','admin'),
	 ('8a9383827ec4e745017ec4e9c0970001','$2a$10$9Ik1LsPV6DKOCAjX0B8tu.zzHtT6UN8BwoTFvG/ueMjs9/pkHyuO2','manager','manager'),
	 ('8a9383827ec4eb5c017ec51b91cf0000','$2a$10$pjPU/912C.Q0jkYODu8T8OWHnF7cdNvRXZpBZGDKYQWBdcM3OO7zm','client','client');