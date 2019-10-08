-- Lidar Visualization And Selection Tool
create user lvast with password 'lvast';
grant all privileges on database lvast to lvast;
-- for main menu where you chose which data to take
create table if not exists main_menu_storage (
id bigserial primary key ,
identification_name varchar not null
);
-- todo create index
-- here will be stored raw data about each of road videos
create table if not exists raw_data_store (
id bigserial primary key,
raw_data_array varchar not null,
main_menu_id serial not null,
data_id serial not null,
foreign key (main_menu_id) references main_menu_storage(id)
);
-- selected items
create table if not exists selected_item_name(
id bigserial primary key ,
item_name varchar  not null
);
-- selected items pictures
create table if not exists selected_item_pictures(
id bigserial primary key ,
picture_name varchar not null
);
-- todo create index
-- here will be stored data of selected items
create table if not exists selected_data_parts(
id bigserial primary key ,
selected_item_name_id serial not null,
selected_item_picture_id bigserial not null,
raw_selected_data varchar not null,
raw_data_id bigserial not null,
foreign key (selected_item_name_id) references selected_item_name(id),
foreign key (selected_item_picture_id) references selected_item_pictures(id),
foreign key (raw_data_id) references raw_data_store(id)
);
