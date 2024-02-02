# Resolute
A fitness tracking/logging application with social media aspects. The purpose of such an application is to encourage users and their friends to be involved in each other's fitness endeavors through non-intrusive mobile notifications.

The primary function of Resolute is to remind users to be physically active via notifications. These can come from the app itself, but will also come from other users such as contacts or friends. When a user completes a workout it will notify their contacts with a push-notification from Resolute of their achievement. The friend is prompted for a one-button auto-reply such as “Great work!” or “Impressive!”. This subtly encourages the user's friends to exercise alongside them, while adding motivation for the original user to keep at their exercises without requiring any more input besides logging their workout.

## Project Repository Description
**ResoluteApp/app** is the primary directory for application source code, as well as some additional files automatically generated and required for compilation of the application.<br /> 
  * app/src contains the source code as well as testing code generated for use in Android Studio. This directory contains the file "AndroidManifest.xml", the compilation instructions for this application on Android systems.<br />
    * src/main/java contains .java files associated with the project, primarily consisting of either Fragment or Activity classes.<br />
    * src/main/res is the resources folder with more specified directories contained within.<br />
      * res/drawable holds image files for use in the application, such as backgrounds, icons, and logos.<br />
      * res/layout contains layout, or .xml, files that can be used in Android Studio for visually-focused designing of the application's UI on whole-screens, such as fragments or activities.<br /> 
      * res/menu also contains layout .xml files, but is specifically for menus used accesible from multiple screens in the application.<br /> 
      * res/mipmap* encompasses multiple file formats for the project's home-screen icons for various Android operating system UI schemes.<br />
      * res/navigation contains navigation layout files. For this project, that is primarily "nav_graph.xml", which directs navigation by associating actions with destination fragments and/or activities.
      * res/values contains .xml files for greater organization of multi-use values across the application such as strings (strings.xml) and color definitions (colors.xml).
**ResoluteApp/gradle/wrapper** as well as all remaining files in **ResoluteApp** containing the word "gradle" are used for the definition of gradle specifications for running and compiling the app, such as the app's associated backend and database connections. Most, if not all, of these files are auto-generated upon the creation of the project, and are seldom edited by-hand to prevent fundamental runtime issues and exceptions.

## Setup Instructions
**Programming languages -** Java version 1.8 (A.K.A Java JDK 8), XML 1.1 (default)<br />
**Dependancies -** Written using Android Studio Hedgehog (Version 2023.1.1) for devices running Android API 26 ("Oreo"; Android 8.0) or any later Android API. <br />
**Setup instructions -** 
 * Download Android Studio at https://developer.android.com/studio?gclid=Cj0KCQiAwvKtBhDrARIsAJj-kTietQJlTcLGuP-ZfoUrNfgJU6WJgMoxkebWq8Pgt5XsD34Z0iMi6psaArebEALw_wcB&gclsrc=aw.ds.
 * After installing and opening Android Studio select "Get from VCS" on the main menu.
 * Here on GitHub, copy the clone URL from the "code" menu, and paste this URL in the "URL" textbox in Android Studio.
 * Select "Clone" and wait for Gradle setup to complete.
 * Using either a virtual device located under Tools -> Device Manager in Android Studio, or by connecting a physical device via USB and enabling developer options on that device, selecting either the play button at the top of the window or Run -> Run 'app' the application will be compiled, installed, and opened on the device of choice. 

## The Team: Team Resolute

### Arie Gentry - Team Leader

I am a senior at EMU, formerly majored in Secondary Education Mathematics, but currently majoring in Computer Science. While I currently do not have any particular path I intend my life and career to go, I am utilizing my time at Eastern to try to open as many doors as I reasonably can. I am an optimist with my biggest flaw being my inability to define my limits, and sometimes pushing myself to take on an inhuman amount of work without regard to health or happiness. My coding experiences are all from EMU, so I have baseline experience with Java, Python, C, HTML5, Javascript, PHP, as well as SML and Lisp. This is my last semester at Eastern, and I look forward to working with everybody!

### Jonathan Taylor - Deputy Team Leader

A senior at Eastern Michigan, my degree is in Computer Science and I plan on graduating this semester. I'm interested in this project because my mobile development experience is limited and I've been looking to get some more practice. When I'm not at school or work I enjoy playing cards, reading, and scrolling through all the streaming apps trying to find something to watch. 

### Victoria Ceci - Team Member

I am a junior at EMU double majoring in Data Science and Political Science with plans to graduate in the winter of 2025. My career goal is to become a political analyist for an interest group or news network. My coding experience has all been through Eastern, so I have knowledge primarily in Java and Python. In my free time I enjoy reading, going to concerts, and hanging out with my cat.

### Kailin Bedoway - Team Member

I am an EMU senior majoring in Data Science and Analytics planning to graduate after this semester. I have gained all of my coding knowledge through my schooling, and have experience with languages including Python, Java, Javascript, HTML, CSS, R, and SQL. I am currently unsure of exactly what career path I will take after graduation, but I'm excited to explore the possibilities in the future!

### William Paddison - Team Member

I am a senior at EMU with a major in Computer Science I plan to graduate at the end of this semester in April. I am currently working in the service industry and have career goals of finding a career where I am able to work in a sales position where I can utilized my knowledge of computers to connect people with the technology either physical or digital that improves there life or business. I am super excited for this last semester, I really enjoy getting to know and working with new people :)

### Albaraa Omian - Team Member

(Please update member's description)
