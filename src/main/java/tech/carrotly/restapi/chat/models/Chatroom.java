package tech.carrotly.restapi.chat.models;

import tech.carrotly.restapi.chat.enums.Age;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class Chatroom {
    UUID uuid;
    Age ageOfHelpSeeker;
    Date createdAt;
}
