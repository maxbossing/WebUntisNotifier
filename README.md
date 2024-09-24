# WebUntisNotifier

Send notifications when lessons are cancelled in WebUntis, because they dont seem to be able to do that themselves.

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
  "pushover": {
    "apiKey": "Your pushover API key",
    "groupKey": "The Group/User Key to send notifications to "
  }
}
```

## License

As long as not stated otherwise, WebUntis Notifier is licensed under the GNU Affero General Public 
License, version 3, which requires that any modified version of this software, or software using it,
whether directly or used over a network has to have its code available to its users, in addition to 
the standard GPL requirements for sharing modifications.