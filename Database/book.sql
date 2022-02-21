-- pos_books_db.author definition

CREATE TABLE `author` (
  `id_author` decimal(10,0) NOT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_author`),
  UNIQUE KEY `author_UK` (`last_name`,`first_name`),
  UNIQUE KEY `UKsyl6q0w50pg4cboumv0gse20t` (`last_name`,`first_name`),
  KEY `IDX_auth_first_name` (`first_name`),
  KEY `IDX_auth_last_name` (`last_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- pos_books_db.book definition

CREATE TABLE `book` (
  `isbn` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `publishing_year` decimal(10,0) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `quantity` int DEFAULT '0',
  PRIMARY KEY (`isbn`),
  UNIQUE KEY `UK_title` (`title`),
  KEY `IDX_publishing_year` (`publishing_year`),
  KEY `IDX_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- pos_books_db.hibernate_sequence definition

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- pos_books_db.book_author definition

CREATE TABLE `book_author` (
  `isbn` varchar(20) DEFAULT NULL,
  `id_author` decimal(10,0) DEFAULT NULL,
  `list_index` int DEFAULT NULL,
  KEY `isbn_FK` (`isbn`),
  KEY `id_author_FK` (`id_author`),
  CONSTRAINT `id_author_FK` FOREIGN KEY (`id_author`) REFERENCES `author` (`id_author`),
  CONSTRAINT `isbn_FK` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO pos_books_db.author (id_author,first_name,last_name) VALUES
	 (2,'AuthorTest01-firstName','AuthorTest01-lastName'),
	 (1,'Miguel','Cervantes Saavedra'),
	 (12,'James','Joyce'),
	 (10,'Marcel','Proust'),
	 (11,'Joanne','Rowling'),
	 (13,'Test','Test'),
	 (15,'Test1','Test1'),
	 (17,'Test3','Test3'),
	 (19,'Test4','Test4');
	 
INSERT INTO pos_books_db.book (isbn,title,publisher,publishing_year,category,quantity) VALUES
	 ('1','1','Testing publisher',1790,'sci-fy',0),
	 ('1000_1','Title changed2','Publisher changed',2000,'literature',3),
	 ('1000_2','Test2','Arthur',1605,'literature',10),
	 ('1000_3','Test3','Arthur',1605,'literature',50),
	 ('1000_4','TestBook 4','Just Another Publisher',1990,'sci-fy',0),
	 ('1000_5','TestBook 5','Just Another Publisher',1990,'sci-fy',0),
	 ('1234_1','In Search of Lost Time','Arthur',1913,'literature',0),
	 ('1334_1','Harry Potter and the Goblet of Fire','Corint',2000,'literature',0),
	 ('2','2','Testing publisher',1790,'sci-fy',3),
	 ('2222_1','Don Quixote','Arthur',1605,'literature',0);
INSERT INTO pos_books_db.book (isbn,title,publisher,publishing_year,category,quantity) VALUES
	 ('3','3','Testing publisher',1790,'sci-fy',2),
	 ('4','4','Testing publisher',1790,'sci-fy',0),
	 ('5','5','Testing publisher',1790,'sci-fy',3),
	 ('5000_1','Ulysses','Arthur',1904,'literature',0);
	 
INSERT INTO pos_books_db.book_author (isbn,id_author,list_index) VALUES
	 ('1000_2',13,1),
	 ('1000_2',17,2),
	 ('1000_2',15,3);