# Timezone Service for Firmware-HQ

## Endpoints

`curl https://timezone.firmware-hq.dev/v1/timezone`

Will return the timezone information for the requesting client. Uses the external IP of the client

`curl https://timezone.firmware-hq.dev/v1/timezone/Europe`

Will return all locations within the area.

`curl https://timezone.firmware-hq.dev/v1/timezone/Europe/Berlin`

Will return the timezone information for a specific area/location combination.

## Responses

All endpoint will return a 200 or 404 (in case of an error).
