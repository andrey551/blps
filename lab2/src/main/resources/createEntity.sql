/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Never
 * Created: May 9, 2024
 */
CREATE TABLE USERDB (
    ID SERIAL PRIMARY KEY,
    USERNAME VARCHAR(50),
    PASSWORD VARCHAR(50),
    EMAIL TEXT,
    LAST_LOGIN DATE,
    MEMBER_PASSKEY TEXT
);

CREATE TABLE PAYMENT (
    ID SERIAL PRIMARY KEY,
    USER_ID INT,
    BALANCE FLOAT DEFAULT 0.0,
    WITHDRAWACCOUNT TEXT
);

CREATE TABLE ROLEDB (
    ID SERIAL PRIMARY KEY,
    NAME TEXT
);

CREATE TABLE FILE (
    ID SERIAL PRIMARY KEY,
    DOWNLOAD_CNT INT DEFAULT 0,
    DOWNLOAD_URL TEXT,
    DT_ADDED DATE,
    DT_EXPIRES DATE,
    FILE_PASSWORD TEXT,
    FILENAME_SOURCE TEXT,
    IDSTR TEXT,
    SIZE INT,
    USER_ID INT
);

CREATE TABLE USERS_ROLES (
    USER_ID INT,
    ROLE_ID INT
);