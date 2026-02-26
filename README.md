# 📘 GyanLike – MCQ Quiz Android Application

GyanLike is a structured MCQ-based Android quiz application built with Kotlin and Firebase.  
It supports role-based authentication (Admin & User), real-time database updates, timed exams, score tracking, and hierarchical subject management.

This project demonstrates strong Android development fundamentals, Firebase integration, and scalable database design.

---

## 🚀 App Overview

GyanLike is designed to provide an organized and scalable quiz system where:

- Admins can manage questions dynamically
- Users can attempt timed quizzes
- Scores are calculated instantly
- Data syncs in real-time using Firebase

The app follows a structured flow:

**Subject → Chapter → Topic → MCQs**

---

## ✨ Core Features

### 🔐 Authentication System
- Firebase Authentication
- Role-based login (Admin / User)
- Secure session handling

### 👨‍💼 Admin Panel
- Add, Update, Delete MCQs (CRUD operations)
- Manage Subjects, Chapters, Topics
- Real-time question updates
- Cloud-based data management

### 👨‍🎓 User Panel
- Attempt quizzes by Subject / Chapter
- Per-question timer system
- Instant score calculation
- Answer review with explanations
- Persistent score history

### ⏱ Timer-Based Exam System
- Countdown timer per question
- Auto-next functionality
- Prevents answer manipulation

### ☁ Real-Time Cloud Sync
- Firebase Firestore
- Firebase Realtime Database
- Live data updates without app restart

### 📊 Performance Tracking
- Score tracking
- Profile updates
- Cloud-stored results
- Structured progress flow

---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **UI Design:** XML  
- **Architecture:** Activity-based structure  
- **Database:** Firebase Firestore  
- **Realtime Data:** Firebase Realtime Database  
- **Authentication:** Firebase Auth  
- **Storage:** Firebase Cloud Storage  
- **Version Control:** Git & GitHub  

---

## 📂 Project Structure

```
GyanLike/
│
├── app/
│   ├── java/com.gyanlike/
│   │   ├── activities/
│   │   ├── adapters/
│   │   ├── models/
│   │   ├── admin/
│   │   └── utils/
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   └── values/
│   └── AndroidManifest.xml
│
├── build.gradle
└── settings.gradle
```

---

## 🔒 Firebase Setup Required

For security reasons, `google-services.json` is NOT included in this repository.

To run the project:

1. Create a Firebase project
2. Enable:
   - Email/Password Authentication
   - Firestore Database
   - Realtime Database
3. Download `google-services.json`
4. Place it inside the `app/` directory
5. Sync Gradle and run

---

## 🎯 What This Project Demonstrates

- Role-based authentication implementation
- Scalable Firestore data modeling
- Real-time database synchronization
- CRUD operations with cloud integration
- Timer-based quiz logic
- Clean UI/UX structuring
- Secure user session handling

---

## 📌 Future Improvements

- MVVM Architecture implementation
- Push Notifications (FCM)
- Detailed analytics dashboard
- Leaderboard system
- Unit testing integration

---

## 👨‍💻 Developer

**Mayank Bhadakan**  
Master’s in Computer Science  
Android Developer | Kotlin | Firebase  

LinkedIn: https://www.linkedin.com/in/mayank-bhadakan
