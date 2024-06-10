/**********************************/
/* Table Name: ?��?�� */
/**********************************/
CREATE TABLE MEMBER(
		MEMBERNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ID                            		VARCHAR2(30)		 NOT NULL,
		PASSWD                        		VARCHAR2(200)		 NOT NULL,
		MNAME                         		VARCHAR2(30)		 NOT NULL,
		TEL                           		VARCHAR2(14)		 NOT NULL,
		ZIPCODE                       		VARCHAR2(5)		 NULL ,
		ADDRESS1                      		VARCHAR2(80)		 NULL ,
		ADDRESS2                      		VARCHAR2(50)		 NULL ,
		MDATE                         		DATE		 NOT NULL,
		GRADE                         		NUMBER(2)		 NOT NULL,
		POINT                         		NUMBER(10)		 NOT NULL,
        BIRTH                               NUMBER(10)		 NOT NULL,
  CONSTRAINT SYS_C008567 UNIQUE (ID)
);

commit;

COMMENT ON TABLE MEMBER is '?��?��';
COMMENT ON COLUMN MEMBER.MEMBERNO is '?��?�� 번호';
COMMENT ON COLUMN MEMBER.ID is '?��?��?��';
COMMENT ON COLUMN MEMBER.PASSWD is '?��?��?��?��';
COMMENT ON COLUMN MEMBER.MNAME is '?���?';
COMMENT ON COLUMN MEMBER.TEL is '?��?��번호';
COMMENT ON COLUMN MEMBER.ZIPCODE is '?��?��번호';
COMMENT ON COLUMN MEMBER.ADDRESS1 is '주소1';
COMMENT ON COLUMN MEMBER.ADDRESS2 is '주소2';
COMMENT ON COLUMN MEMBER.MDATE is '�??��?��';
COMMENT ON COLUMN MEMBER.GRADE is '?���?';
COMMENT ON COLUMN MEMBER.PROFILE is '?��?�� ?��로필 ?��미�?';
COMMENT ON COLUMN MEMBER.POINT is '?��?��?��';



CREATE SEQUENCE member_seq
  START WITH 1              -- ?��?�� 번호
  INCREMENT BY 1          -- 증�?�?
  MAXVALUE 9999999999 -- 최�?�?: 9999999 --> NUMBER(7) ???��
  CACHE 2                       -- 2번�? 메모리에?���? 계산
  NOCYCLE;                     -- ?��?�� 1�??�� ?��?��?��?�� 것을 방�?
 