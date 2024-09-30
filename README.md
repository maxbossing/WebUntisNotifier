# WebUntisNotifier

Send notifications when lessons are cancelled in WebUntis, because they dont seem to be able to do that themselves.

> For support and feature requests, [join my discord](https://discord.com/invite/qETwkZWZrf)!

## Usage
> You will probably have to adapt LessonParser.kt/lessonMap to fit your schedule, as it is currently hardcoded on the times of mine

1. Build the Project
2. Build the Docker Container
3. Mount your config.json at /config.json
4. Start the container
5. ...
6. Profit

## Configuration

```json
{
  "untis": {
    "school": "The name your school uses in WebUntis",
    "username": "Your WebUntis username",
    "password": "Your WebUntis password",
    "server": "The WebUntis Server of your school (ex: https://hektor.webuntis.com)"
  },
  "notifications": {
    see ###notifications
  }
}
```
### Notifications
WebUntisNotifier currently only supports Pushover and Ntfy as notification provider. 
> If you need/want a different one, add it (its very easy!) or hit me up on my discord!
#### Pushover
```json
"notifications": {
  "type": "Pushover",
  "apiKey": "Your api key",
  "groupKey": "Your group to send messages to"      
}
```
#### Ntfy
```json
"notifications": {
  "type": "Ntfy",
  "url": "Your ntfy URL",
  "topic": "The ntfy topic to send messages to",
  "username": "Username for your account. If set to null, password will be treated as a bearer token",
  "password": "Your password/Bearer token"
}
```

## Todo
- [ ] More notification providers
- [ ] Support for Lesson Messages
- [ ] Detect Moved lessons
- [ ] Public docker containers

## License

As long as not stated otherwise, WebUntis Notifier is licensed under the GNU Affero General Public 
License, version 3, which requires that any modified version of this software, or software using it,
whether directly or used over a network has to have its code available to its users, in addition to 
the standard GPL requirements for sharing modifications.