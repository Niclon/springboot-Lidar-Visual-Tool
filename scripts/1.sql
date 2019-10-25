CREATE TABLE if not exists raw_data_pictures(
                                                id bigserial primary key,
                                                front_picture_path varchar,
                                                back_picture_path varchar,
                                                raw_data_id bigserial not null,
                                                foreign key (raw_data_id) references raw_data_store(id)
);