-- Real life URLs can be up to 1024 chars if I'm not mistaken
alter table video change imageUrl imageUrl varchar(1024) DEFAULT NULL COMMENT 'URL of preview image';
alter table video change sourceUrl sourceUrl varchar(1024) DEFAULT NULL COMMENT 'For playerType JW';
-- index is limited to first 255 characters; more than that isn't possible
alter table Video add constraint unique index idx_sourceUrl (sourceUrl(255));
-- user can now be admin
alter table user add column isAdmin tinyint(1) null;
-- an admin user
insert into user
(creationDate, email, name, password, dateOfBirth, usernameFacebook, usernameHyves, usernameTwitter, gender, isAdmin)
values
('2013-03-14 00:00:00', 'maintenance@beeldengeluid.nl', 'maintenance', '4ubpKwX8/o2vt/AHLpZY8KF9E16jEmAwObYA/N/huuv8Kou6T7mLwF77JyDir8gg', null, null, null, null, null, 1);
-- video table must support rollbacks now, so convert table engine to InnoDB
ALTER TABLE Video ENGINE=InnoDB;
