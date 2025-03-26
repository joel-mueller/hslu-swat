USE mydatabase;

CREATE TABLE IF NOT EXISTS `books` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`isbn` VARCHAR(25) NOT NULL,
	`title` VARCHAR(255),
	`author` VARCHAR(255),
	`year` VARCHAR(255),
	`publisher` VARCHAR(255),
	`imageUrlS` VARCHAR(255),
	`imageUrlM` VARCHAR(255),
	`imageUrlL` VARCHAR(255),
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `customers` (
	`id` VARCHAR(50) NOT NULL,
	`first_name` VARCHAR(255),
	`last_name` VARCHAR(255),
	`street` VARCHAR(255),
	`zip_code` VARCHAR(50),
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `borrow_records` (
	`uuid` VARCHAR(50) NOT NULL,
	`book_id` INT,
	`customer_id` VARCHAR(50),
	`date_borrowed` VARCHAR(50),
	`duration_days` INT,
	`returned` TINYINT(1),
	PRIMARY KEY (`uuid`),
	FOREIGN KEY (`book_id`) REFERENCES books(`id`),
	FOREIGN KEY (`customer_id`) REFERENCES customers(`id`)
);