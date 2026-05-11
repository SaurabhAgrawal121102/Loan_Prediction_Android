# 🏦 Loan Prediction Android App (ML Integrated)

A modern Android application that uses a **Machine Learning model** (Scikit-Learn) to predict loan approval directly on the device. This project utilizes the **Chaquopy** plugin to execute Python logic seamlessly within the Android ecosystem, eliminating the need for an external API.

## 🚀 Key Features
- **On-Device Prediction**: Runs a Python-based Scikit-Learn model locally using Chaquopy.
- **Modern UI/UX**: Built with a polished, responsive interface featuring Material Design 3.
- **Async Execution**: Predictions are handled on a background thread to ensure a smooth, non-blocking UI.
- **Real-time Probability**: Not only predicts "Approved" or "Rejected" but also provides the confidence score (probability).
- **Input Validation**: Robust handling of user inputs for all 12 financial features.

## 🛠️ Technology Stack
- **Android**: Java, Android SDK
- **Python**: 3.10
- **ML Libraries**: Scikit-Learn, Pandas, NumPy, Joblib
- **Integration**: Chaquopy (Python SDK for Android)

## 📋 Prerequisites
To build this project from source, your development machine needs:
1. **Android Studio** (Koala or newer recommended).
2. **Python 3.10** installed on your system (Required by the Chaquopy build process).
   - *Important*: Ensure Python is added to your system's `PATH`.

## ⚙️ Installation & Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/SaurabhAgrawal12112002/LoanPrediction-Android.git
   ```
2. **Open in Android Studio**:
   - Wait for the Gradle sync to complete.
   - The project will automatically download the necessary Python wheels (Pandas, Scikit-Learn) for Android.
3. **Run the app**:
   - Connect your Android device or start an emulator (`arm64-v8a` recommended).
   - Click **Run**.

## 📂 Project Structure
- `app/src/main/python/`: Contains the Python ML logic (`predict.py`) and the trained model (`loan_prediction_model.pkl`).
- `app/src/main/java/`: Contains the Android Activity logic and Chaquopy integration.
- `app/src/main/res/layout/`: Modern XML layouts for the user interface.

## 🧠 Model Information
The model is a **Classifier** trained on the classic Loan Prediction dataset. It processes 12 features including Applicant Income, Credit History, Education, and Property Area to determine eligibility.

## 📝 License
This project is for educational purposes. Feel free to use and modify it for your own learning!
