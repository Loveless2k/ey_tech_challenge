create table phones(
    id UUID,
    number varchar(10),
    citycode varchar(5),
    countrycode varchar(5),
    user_id UUID,
    foreign key (user_id) references users(id)
)