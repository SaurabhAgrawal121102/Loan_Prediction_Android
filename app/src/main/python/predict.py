import joblib
import numpy as np
import pandas as pd
import os

# load model
model_path = os.path.join(
    os.path.dirname(__file__),
    "loan_prediction_model.pkl"
)

model = joblib.load(model_path)


def predict_loan(
        gender,
        married,
        education,
        employed,
        credit,
        dependents,
        semiurban,
        urban,
        applicantIncome,
        coApplicantIncome,
        loanAmount,
        loanTerm
):

    # create dataframe exactly same order as training
    data = pd.DataFrame([[
        gender,
        married,
        dependents,
        education,
        employed,
        applicantIncome,
        coApplicantIncome,
        loanAmount,
        loanTerm,
        credit,
        semiurban,
        urban
    ]], columns=[
        "gender",
        "married",
        "dependents",
        "education",
        "self_employed",
        "applicantincome",
        "coapplicantincome",
        "loanamount",
        "loan_amount_term",
        "credit_history",
        "property_area_Semiurban",
        "property_area_Urban"
    ])

    # prediction
    prediction = model.predict(data)[0]

    # probability
    probability = model.predict_proba(data)[0][1]

    if prediction == 1:
        result = "Loan Approved"
    else:
        result = "Loan Rejected"

    return f"{result}\nProbability : {probability:.2f}"