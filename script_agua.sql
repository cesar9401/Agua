-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema agua
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema agua
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `agua` DEFAULT CHARACTER SET utf8mb4 ;
USE `agua` ;

-- -----------------------------------------------------
-- Table `agua`.`socio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`socio` (
  `socio_id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `dpi` VARCHAR(13) NULL,
  `phone` VARCHAR(8) NULL,
  `address` VARCHAR(100) NULL,
  `type` ENUM('SOCIO', 'MANCOMUNADO') NOT NULL DEFAULT 'SOCIO',
  `exonerated` TINYINT NULL DEFAULT 0,
  `status` TINYINT NULL DEFAULT 1,
  `socio_socio_id` INT NULL,
  PRIMARY KEY (`socio_id`),
  INDEX `fk_socio_socio_idx` (`socio_socio_id` ASC),
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  CONSTRAINT `fk_socio_socio`
    FOREIGN KEY (`socio_socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- datos socio
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('1', 'S-1', 'Juan Carlos', 'Plata', '3456781320901', '77651234', 'Ciudad de Quetzaltenango', 'SOCIO', '0', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('2', 'S-2', 'Carlos Humberto', 'Ruiz', '3456787640901', '77651235', 'Zona 7, Ciudad de Quetzaltenango', 'SOCIO', '0', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`, `socio_socio_id`) VALUES ('3', 'M-1', 'Juan Carlos', 'Plata Jr', '3356781320901', '77651234', 'Ciudad de Quetzaltenango', 'MANCOMUNADO', '0', '1', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('4', 'S-3', 'Jose Jose', 'Perez', '3456781320501', '77651290', '12 Av 3ra Calle Zona 3, Quetzaltenango', 'SOCIO', '0', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('5', 'S-4', 'George', 'Michael', '3456781326501', '77650987', '12 Av 3ra Calle Zona 4, Quetzaltenango', 'SOCIO', '1', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`, `socio_socio_id`) VALUES ('6', 'M-2', 'Ricardo', 'Andrade', '3456781320902', '53519801', 'Zona 7, Ciudad de Quetzaltenango', 'MANCOMUNADO', '0', '1', '4');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('7', 'S-5', 'Dua Fernanda', 'Lipa', '2456787640901', '56789000', '3ra Calle 5-19 Zona 2, Quetzaltenango', 'SOCIO', '0', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('8', 'S-6', 'Aaron', 'Smith', '2556787640901', '77657610', '2da Calle 5-19 Zona 2, Quetzaltenango', 'SOCIO', '0', '1');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`) VALUES ('9', 'S-7', 'Guadalupe', 'Esparza', '2556787641201', '77650987', '1ra Calle 2-19 Zona 2, Quetzaltenango', 'SOCIO', '0', '0');
INSERT INTO `agua`.`socio` (`socio_id`, `code`, `name`, `last_name`, `dpi`, `phone`, `address`, `type`, `exonerated`, `status`, `socio_socio_id`) VALUES ('10', 'M-3', 'Luisa', 'Smith', '2612787641201', '54546789', '9na Calle 2-19 Zona 2, Quetzaltenango', 'MANCOMUNADO', '0', '1', '8');
-- fin datos socio

-- -----------------------------------------------------
-- Table `agua`.`administrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`administrator` (
  `administrator_id` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(50) NOT NULL,
  `status` TINYINT NULL DEFAULT 1,
  `socio_id` INT NOT NULL,
  INDEX `fk_administrator_socio1_idx` (`socio_id` ASC),
  PRIMARY KEY (`administrator_id`),
  CONSTRAINT `fk_administrator_socio1`
    FOREIGN KEY (`socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- datos administrator
INSERT INTO `agua`.`administrator` (`administrator_id`, `password`, `status`, `socio_id`) VALUES ('1', '123321', '1', '1');
INSERT INTO `agua`.`administrator` (`administrator_id`, `password`, `status`, `socio_id`) VALUES ('2', 'abc', '1', '2');
INSERT INTO `agua`.`administrator` (`administrator_id`, `password`, `status`, `socio_id`) VALUES ('3', 'qwerty', '0', '5');
INSERT INTO `agua`.`administrator` (`administrator_id`, `password`, `status`, `socio_id`) VALUES ('4', 'xyz-123', '1', '10');
-- fin datos administrator


-- -----------------------------------------------------
-- Table `agua`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`payment` (
  `payment_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`payment_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `agua`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`event` (
  `event_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `event_date` DATE NOT NULL,
  `event_time` TIME NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `description` TEXT NULL,
  `status` TINYINT NULL DEFAULT 1,
  PRIMARY KEY (`event_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `agua`.`socio_payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`socio_payment` (
  `socio_payment_id` INT NOT NULL AUTO_INCREMENT,
  `socio_id` INT NOT NULL,
  `payment_id` INT NOT NULL,
  `payment_month` DATE NOT NULL COMMENT 'Hace referencia al mes que el socio debe cancelar',
  `amount` DECIMAL(10,2) NOT NULL,
  `payment_date` DATE NULL COMMENT 'Hace referencia a la fecha en la que se hace el pago',
  `administrator_id` INT NOT NULL,
  `code` INT NOT NULL,
  PRIMARY KEY (`socio_payment_id`),
  INDEX `fk_socio_payment_socio1_idx` (`socio_id` ASC),
  INDEX `fk_socio_payment_payment1_idx` (`payment_id` ASC),
  INDEX `fk_socio_payment_administrator1_idx` (`administrator_id` ASC),
  CONSTRAINT `fk_socio_payment_socio1`
    FOREIGN KEY (`socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_socio_payment_payment1`
    FOREIGN KEY (`payment_id`)
    REFERENCES `agua`.`payment` (`payment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_socio_payment_administrator1`
    FOREIGN KEY (`administrator_id`)
    REFERENCES `agua`.`administrator` (`administrator_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `agua`.`socio_event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`socio_event` (
  `socio_event_id` INT NOT NULL AUTO_INCREMENT,
  `socio_id` INT NOT NULL,
  `event_id` INT NOT NULL,
  `administrator_id` INT NOT NULL,
  `payment_date` DATE NULL COMMENT 'Hace referencia a la fecha en la que se pago la multa por no asister, NULL = no ha pagado',
  `description` TEXT NULL,
  `code` INT NOT NULL,
  PRIMARY KEY (`socio_event_id`),
  INDEX `fk_socio_event_socio1_idx` (`socio_id` ASC),
  INDEX `fk_socio_event_event1_idx` (`event_id` ASC),
  INDEX `fk_socio_event_administrator1_idx` (`administrator_id` ASC),
  CONSTRAINT `fk_socio_event_socio1`
    FOREIGN KEY (`socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_socio_event_event1`
    FOREIGN KEY (`event_id`)
    REFERENCES `agua`.`event` (`event_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_socio_event_administrator1`
    FOREIGN KEY (`administrator_id`)
    REFERENCES `agua`.`administrator` (`administrator_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `agua`.`configuration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`configuration` (
  `configuration_id` INT NOT NULL AUTO_INCREMENT,
  `item` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`configuration_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `agua`.`fine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`fine` (
  `fine_id` INT NOT NULL,
  `socio_id` INT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `fine_date` DATE NOT NULL COMMENT 'Hace referencia a la fecha en que se establecio la multa',
  `fine_date_payment` DATE NULL DEFAULT NULL COMMENT 'Hace referencia a la fecha en que se paga la multa',
  `description` TEXT NOT NULL,
  `administrator_id` INT NOT NULL,
  `code` INT NOT NULL,
  PRIMARY KEY (`fine_id`),
  INDEX `fk_fine_socio1_idx` (`socio_id` ASC),
  INDEX `fk_fine_administrator1_idx` (`administrator_id` ASC),
  CONSTRAINT `fk_fine_socio1`
    FOREIGN KEY (`socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_fine_administrator1`
    FOREIGN KEY (`administrator_id`)
    REFERENCES `agua`.`administrator` (`administrator_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
