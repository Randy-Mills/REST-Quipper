# REST quipper

Quipper will let you manage your quips. You can add quips to your quips database, retrieve random quips, and get a count of how many quips you've collected. 

If you find yourself feeling especially nihilistic you can even drop all your quips and start over.

## Usage

>    $> java -jar quipper-0.1.0-standalone.jar [args]

or, if you're in development mode

>    $> lein run -- [args]

## Options

    -h,--help Prints the help information
    -f,--file provides an alternate file for quip storage.
        (used in testing but you can use it, too)
	-p,--port Provide to port to run quipper web service on

## Routes

### `POST /quips`

Takes a JSON object of quips to create.

**Request**:

```json
{
  "quips":
   [
      {"quip": "If at first you don't succeed, so much for skydiving."},
      {"quip": "Two guys walk into a bar.  You'd think the second one would have ducked!"}
   ]
}
```

**Response**:

*HTTP 201*
```json
{
  "quips":
   [
      {"quip": "If at first you don't succeed, so much for skydiving."},
      {"quip": "Two guys walk into a bar.  You'd think the second one would have ducked!"}
   ]
}
```


### `GET /quips/random`

Returns a random quip (or empty if none avail)

**Response**:

*HTTP 200*
```json
{
  "quip": "If at first you don't succeed, so much for skydiving."
}
```

### `GET /quips/count`

Returns the number of quips stored.

**Response**:

*HTTP 200*
```json
{
  "count": 3
}
```

### `DELETE /quips`

Drops the current database of quips.

*Sometimes you just want to watch it all burn.*

**Response**:

*HTTP 204*


## Learning Resources

You should read about [Compojure](https://github.com/weavejester/compojure/wiki) and [Ring](https://github.com/ring-clojure/ring/wiki).