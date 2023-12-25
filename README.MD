### Prerequisite

> java 21\
> maven (tested in 3.9.4)
---

### Steps To Clone And Run

> 1. Clone via https://github.com/wq-chang/swe-challenge.git
> 2. Use command to run: mvn spring-boot:run

---

### H2 Url

> http://localhost:8080/h2 \
> JDBC URL: jdbc:h2:mem:mydb\
> Username: root
---

### API 1 (Register User)

> GET http://localhost:8080/users/register

Sample Request

```json
{
  "username": "owner",
  "displayName": "owner"
}
```

Sample Response

```json
{
  "username": "owner",
  "displayName": "owner"
}
```

---

### API 2 (Initiate Session)

> POST http://localhost:8080/sessions/initiate

Sample Request

```json
{
  "username": "owner"
}
```

Sample Response

```json
{
  "uuid": "47a1b0ca-ec2a-47e5-8445-be7a45a2b93b",
  "ownerDisplayName": "owner",
  "active": true,
  "pickedRestaurant": null
}
```

---

### API 3 (Invite Users To Join Session)

> POST http://localhost:8080/sessions/invite-users

Sample Request

```json
{
  "ownerUsername": "owner",
  "inviteeUsernames": [
    "inviteeA",
    "inviteeB",
    "inviteeC"
  ]
}
```

Sample Response

```json
{
  "newInvitedUsers": [
    "inviteeA",
    "inviteeB"
  ],
  "notFoundUsers": [
    "inviteeC"
  ],
  "invitedUsers": []
}
```

---

### API 4 (Search Pending Invitations)

> POST http://localhost:8080/invitations/search

Sample Request\
accepted: if the invitee has joined the session\
sessionActive: if the session is active or ended

```json
{
  "inviteeUsername": "inviteeA",
  "accepted": [
    false
  ],
  "sessionActive": [
    true
  ]
}
```

Sample Reponse

```json
[
  {
    "sessionUuid": "268ddd68-1bfc-4f0d-a023-40b967cb0986",
    "invitationUuid": "82f02feb-f186-45f9-b180-e3b9064fa29a",
    "ownerUsername": "owner",
    "sessionActive": true,
    "accepted": false,
    "restaurant": null,
    "pickedRestaurant": null
  }
]
```

---

### API 5 (Accept Invitation)

> POST http://localhost:8080/invitations/accept

Sample Request

```json
{
  "invitationUuid": "15b4bebe-18ac-46d0-bfc1-64565e024fee",
  "inviteeUsername": "inviteeA"
}
```

Sample Response

```json
{
  "sessionUuid": "4b5afeb6-5cd2-4f5c-a4aa-5f2a158b622f",
  "invitationUuid": "15b4bebe-18ac-46d0-bfc1-64565e024fee",
  "ownerUsername": "owner",
  "sessionActive": true,
  "accepted": true,
  "restaurant": null,
  "pickedRestaurant": null
}
```

---

### API 6 (Suggest Restaurant)

> POST http://localhost:8080/invitations/suggest-restaurant

Sample Request

```json
{
  "invitationUuid": "15b4bebe-18ac-46d0-bfc1-64565e024fee",
  "inviteeUsername": "inviteeA",
  "restaurant": "restaurant"
}
```

Sample Response

```json
{
  "sessionUuid": "4b5afeb6-5cd2-4f5c-a4aa-5f2a158b622f",
  "invitationUuid": "15b4bebe-18ac-46d0-bfc1-64565e024fee",
  "ownerUsername": "owner",
  "sessionActive": true,
  "accepted": true,
  "restaurant": "restaurant",
  "pickedRestaurant": null
}
```

---

### API 7 (End Session)

> POST http://localhost:8080/sessions/end

Sample Request

```json
{
  "username": "owner"
}
```

Sample Response

```json
{
  "uuid": "4b5afeb6-5cd2-4f5c-a4aa-5f2a158b622f",
  "ownerDisplayName": "owner",
  "active": false,
  "pickedRestaurant": "restaurant"
}
```

---

### API 8 (List Restaurant)

> GET http://localhost:8080/invitations/{invitee-username}/{accepted-invitation-uuid}/restaurants

Sample Request

```
http://localhost:8080/invitations/inviteeA/15b4bebe-18ac-46d0-bfc1-64565e024fee/restaurants
```

Sample Response

```json
[
  "restaurant"
]
```

---

### Some Design Decisions

> Used H2 to reduce dependencies, should use other db to decouple the data store and app to ensure
> app scalability