# Prism api documentation

## Root link  /prism/v1

## Authentification

### Registration 
post: /registration

| Property         | Type           |
|------------------|----------------|
| login            | string         |
| password         | string         |
| confirmPassword | string         |
| isAdmin         | boolean / null | 

return type

| Property | Type    |
|----------|---------|
| id       | number  |
| login    | string  |
| isAdmin  | boolean | 

### Login 
post: /login

| Property | Type   |
|----------|--------|
| login    | string |
| password | string |

return type

| Property | Type    |
|----------|---------|
| id       | number  |
| login    | string  |
| isAdmin  | boolean | 

### Logout 
delete: /logout

### Get current user 
get: /user

| Property | Type    |
|----------|---------|
| id       | number  |
| login    | string  |
| isAdmin | boolean | 

### Delete user 
delete: /user/{id}

### Edit user login
patch: /user/{id}/login

| Property | Type           |
|----------|----------------|
| login    | string         |

return type

| Property | Type    |
|----------|---------|
| id       | number  |
| login    | string  |
| admin | boolean | 

### Edit user password
patch: /user/{id}/password

| Property | Type           |
|----------|----------------|
| password | string         |

return type

| Property | Type    |
|----------|---------|
| id       | number  |
| login    | string  |
| isAdmin  | boolean | 

## Chat

## Message

## User