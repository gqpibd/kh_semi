CREATE TABLE MEMBER(
    NAME VARCHAR2(20) NOT NULL,
    ID VARCHAR2(10) PRIMARY KEY,
    PW VARCHAR2(20) NOT NULL,
    COUPON NUMBER(1),
    AUTH NUMBER(1) NOT NULL,
    ADR VARCHAR2(50) NOT NULL,
    PHONE VARCHAR2(20) NOT NULL
);

insert into member (name,id,pw,auth,adr,phone)
values ('admin', 'admin', 'admin', 1, 'KH CHICKEN', '010-0000-0000');


select * from member;

CREATE TABLE MENU(
    MENU_NAME VARCHAR2(15) PRIMARY KEY,
    PRICE NUMBER(5) NOT NULL
);

CREATE TABLE ORDER_DETAIL(
    ID VARCHAR2(10),
    MENU_NAME VARCHAR2(15),
    COUNT NUMBER(10) NOT NULL,
    BEV_COUPON NUMBER(10),
    ORDER_DATE DATE NOT NULL,
    REVIEW VARCHAR2(1000),
    SCORE NUMBER(5),
    CONSTRAINT FK_ID FOREIGN KEY(ID)
    REFERENCES MEMBER(ID),
    CONSTRAINT FK_MENU FOREIGN KEY(MENU_NAME)
    REFERENCES MENU(MENU_NAME)
);

   

