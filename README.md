# Getting Started with BusinessConnect in Java on Heroku

## Introduction

This tutorial will have you deploying a Java BusinessConnect server in minutes.

Hang on for a few more minutes to learn how it all works, so you can make the most out of LINE platform.

The tutorial assumes that you have:

* a free [Heroku account](https://signup.heroku.com/signup/dc)
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/) installed
* [Maven 3](http://maven.apache.org/download.html) installed

## Set up

In this step you will install the Heroku Toolbelt. This provides you access to the Heroku Command Line Interface (CLI),
which can be used for managing and scaling your applications and add-ons. A key part of the toolbelt is the `heroku
local` command, which can help in running your applications locally.

[Download the Heroku Toolbelt](https://toolbelt.heroku.com)

Once installed, you can use the `heroku` command from your command shell.

Log in using the email address and password you used when creating your Heroku account:

```console
$ heroku login
Enter your Heroku credentials.
Email: java@linecorp.com
Password:
```

Authenticating is required to allow both the heroku and git commands to operate.

## Prepare the sample BusinessConnect application

In this step, you will prepare a simple BusinessConnect application that can be deployed.

Execute the following commands to clone the sample application:

```console
$ git clone https://github.com/LINE-Platform/bc-getting-started.git
$ cd bc-getting-started
```

You now have a functioning git repository that contains a simple application as well as a `pom.xml` file, which is used
by Java’s dependency manager, Maven.

## Deploy the sample BusinessConnect application

In this step you will deploy the BusinessConnect application to Heroku.

Create an application on Heroku, which prepares Heroku to receive your source code:

```console
$ heroku create
Creating guarded-atoll-4857... done, stack is cedar-14
https://guarded-atoll-4857.herokuapp.com/ | https://git.heroku.com/guarded-atoll-4857.git
Git remote heroku added
```

When you create an application, a git remote (called `heroku`) is also created and associated with your local git
repository.

Heroku generates a random name (in this case `guarded-atoll-4857`) for your application, or you can pass a parameter to
specify your own application name.

Now deploy your code:

```console
$ git push heroku master
Counting objects: 108, done.
Delta compression using up to 8 threads.
Compressing objects: 100% (37/37), done.
Writing objects: 100% (108/108), 19.72 KiB | 0 bytes/s, done.
Total 108 (delta 48), reused 105 (delta 48)
remote: Compressing source files... done.
remote: Building source:
remote: 
remote: -----> Java app detected
remote: -----> Installing OpenJDK 1.8... done
remote: -----> Installing Maven 3.3.3... done
remote: -----> Executing: mvn -B -DskipTests clean dependency:list install
remote:        [INFO] Scanning for projects...
... (detail messages skipped) ...
remote:        [INFO] ------------------------------------------------------------------------
remote:        [INFO] BUILD SUCCESS
remote:        [INFO] ------------------------------------------------------------------------
remote:        [INFO] Total time: 15.134 s
remote:        [INFO] Finished at: 2015-11-08T10:27:03+00:00
remote:        [INFO] Final Memory: 28M/230M
remote:        [INFO] ------------------------------------------------------------------------
remote: -----> Discovering process types
remote:        Procfile declares types -> web
remote: 
remote: -----> Compressing... done, 65.0MB
remote: -----> Launching... done, v5
remote:        https://guarded-atoll-4857.herokuapp.com/ deployed to Heroku
remote: 
remote: Verifying deploy.... done.
To https://git.heroku.com/guarded-atoll-4857.git
 * [new branch]      master -> master
```

The BusinessConnect application is now deployed. Ensure that at least one instance of the app is running:

```console
$ heroku ps:scale web=1
```

## Define the channel information as environment variables

It is not a good idea to put your channel information such as channel secret into git. You may define the confidential
information as heroku environment variables and store these variables into a `.env` file for future usage.

Add the heroku gem plugin:

```console
$ heroku plugins:install git://github.com/ddollar/heroku-config.git
Installing git://github.com/ddollar/heroku-config.git... done
```

There should be a line `.env` in file `.gitignore` to prohibit `.env` file being uploaded to git.

Now you can set your channel secret and channel access token:

```console
$ heroku config:set CHANNEL_SECRET=<your channel secret>
Setting config vars and restarting guarded-atoll-4857... done
$ heroku config:set CHANNEL_ACCESS_TOKEN=<your channel access token>
Setting config vars and restarting guarded-atoll-4857... done
```

Store these setting into `.env` file:

```console
$ heroku config:pull
Config for guarded-atoll-4857 written to .env
```

You may execute `heroku config:push` to restore these setting to your heroku host. More information about heroku
configuration pease refer to [heroku-config](https://github.com/ddollar/heroku-config).

## View logs

Heroku treats logs as streams of time-ordered events aggregated from the output streams of all your application and
Heroku components, providing a single channel for all of the events.

View information about your running app using one of the [logging commands](https://devcenter.heroku.com/articles/logging),
`heroku logs`:

```console
$ heroku logs --tail
2015-07-06T08:44:45.008841+00:00 heroku[web.1]: Starting process with command `java -cp target/classes:target/dependency/* Main`
2015-07-06T08:44:47.941949+00:00 app[web.1]: [Thread-0] INFO spark.webserver.SparkServer - == Spark has ignited ...
2015-07-06T08:44:47.949901+00:00 app[web.1]: [Thread-0] INFO org.eclipse.jetty.server.Server - jetty-9.0.2.v20130417
2015-07-06T08:44:47.946093+00:00 app[web.1]: [Thread-0] INFO spark.webserver.SparkServer - >> Listening on 0.0.0.0:6243
2015-07-06T08:44:48.280107+00:00 app[web.1]: [Thread-0] INFO org.eclipse.jetty.server.ServerConnector - Started ServerConnector@42d76c7c{HTTP/1.1}{0.0.0.0:6243}
2015-07-06T08:44:48.703339+00:00 heroku[web.1]: State changed from starting to up
```

Visit your application by `curl` command, and you’ll see another log message generated (Please ignore the error message
in the response content of `curl`). Press `Control+C` to stop streaming the logs.

```console
$ curl -v https://guarded-atoll-4857.herokuapp.com/events -d "Any test string"
```
