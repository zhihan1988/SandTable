CREATE TABLE `industry_choice_setting` (
	`id` char(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
	`name` varchar(128),
	`value_set` varchar(255),
	PRIMARY KEY (`id`)
);


CREATE TABLE `industry_expression` (
	`id` char(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
	`name` varchar(128),
	`expression` char(255),
	`initial_value` char(128),
	`step` char(128),
	PRIMARY KEY (`id`)
);

ALTER TABLE `industry_expression` ADD COLUMN `ability` char(128) AFTER `step`;



ALTER TABLE `shapan`.`industry_choice_setting` CHANGE COLUMN `value` `value_set` varchar(255) DEFAULT NULL;
