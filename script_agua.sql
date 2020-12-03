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
  `socio_id` INT NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `dpi` VARCHAR(13) NULL,
  `phone` VARCHAR(8) NULL,
  `address` VARCHAR(100) NULL,
  `type` ENUM('SOCIO', 'MANCOMUNADO') NOT NULL DEFAULT 'SOCIO',
  `exonerated` TINYINT NULL DEFAULT 0,
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


-- -----------------------------------------------------
-- Table `agua`.`administrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `agua`.`administrator` (
  `administrator_id` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(50) NOT NULL,
  `socio_id` INT NOT NULL,
  INDEX `fk_administrator_socio1_idx` (`socio_id` ASC),
  PRIMARY KEY (`administrator_id`),
  CONSTRAINT `fk_administrator_socio1`
    FOREIGN KEY (`socio_id`)
    REFERENCES `agua`.`socio` (`socio_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


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
  `event_id` INT NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
