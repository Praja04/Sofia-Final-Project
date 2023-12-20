
import time
import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import datetime
import urllib
import requests as req
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
from tensorflow import keras
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense, Dropout, Bidirectional
from tensorflow.keras.callbacks import EarlyStopping

data_url = 'https://raw.githubusercontent.com/Praja04/Sofia-Final-Project/main/ML/grocery_sales.csv'
urllib.request.urlretrieve(data_url, 'grocery_sales.csv')
df = pd.read_csv('grocery_sales.csv')

new_df = pd.DataFrame(df)

#def df_user(id):
  #df_user = new_df[new_df['user_id']==id]
  #return df_user

#def get_userid(username):
    #user_data = api.get_user(screen_name=username)
    #user_id = user_data.id_str
    #return user_id

#user =  get_userid()

#new_df = df_user(user)

# Selecting specific columns
#selected_columns = ['createdAt', 'nama_barang', 'total_harga']
selected_columns = ['Order Date', 'Item', 'Sales']
df_selected = new_df[selected_columns]



# Remove rows with NaN values in the 'Sales' column
df_selected = df_selected.dropna(subset=['Sales'])

# Normalizing numerical columns using Min-Max scaling
numerical_columns = ['Sales']
scaler = MinMaxScaler()
df_selected[numerical_columns] = scaler.fit_transform(df_selected[numerical_columns])

# Sort the DataFrame by 'Order Date'
df_selected.sort_values('Order Date', inplace=True)

#distinc the items
items = df_selected['Item'].drop_duplicates(keep = 'first')

# Create a dictionary to store DataFrames for each item
dfs = {}
#make a new df for every items
for item in items:
    item_df = pd.DataFrame(df_selected.loc[df_selected['Item'] == item]).copy
    dfs[item] = item_df


# Create time sequences
def create_sequences(data, sequence_length):
    sequences, targets = [], []
    for i in range(len(data) - sequence_length):
        seq = data.iloc[i:i + sequence_length][['Sales']].values
        target = data.iloc[i + sequence_length]['Sales']
        sequences.append(seq)
        targets.append(target)
    return np.array(sequences), np.array(targets)

def build_model(sequence_length):
    model = Sequential()
    model.add(LSTM(50, input_shape=(sequence_length, 1)))
    model.add(Dense(64, activation='relu'))
    model.add(Dense(sequence_length*6, activation='relu'))
    model.add(Dense(sequence_length*2, activation='relu'))
    model.add(Dense(1))
    model.compile(optimizer='adam', loss='mean_squared_error')
    return model

def run_model(name,df, batch_size, sequence_length):
  # Set sequence length and create sequences
  X, y = create_sequences(df, sequence_length)
  X = np.expand_dims(X, axis=-1)
  # Split the data into train, validation, and test sets
  X_train, X_temp, y_train, y_temp = train_test_split(X, y, test_size=0.3, random_state=42)
  X_val, X_test, y_val, y_test = train_test_split(X_temp, y_temp, test_size=0.5, random_state=42)
  model = build_model(sequence_length)

  early_stopping = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)

  # Train the model
  history = model.fit(X_train, y_train, epochs=50, batch_size=batch_size, validation_data=(X_val, y_val),
                      callbacks=early_stopping
                      )

  y_pred = model.predict(X_test)

  return model

Overall_model = run_model('Overall',df_selected, 128, 20)
Breads_model = run_model('Breads',dfs['Breads'](), 32, 25)
Cakes_model = run_model('Cakes',dfs['Cakes'](),32,15)
Health_Drinks_model = run_model('Health_Drinks',dfs['Health_Drinks'](),32,5)
Chocolates_model = run_model('Chocolates',dfs['Chocolates'](),32,5)
Fish_model = run_model('Fish',dfs['Fish'](),32,15)
Salt_model = run_model('Salt',dfs['Salt'](),32,15)
Noodles_model = run_model('Noodles',dfs['Noodles'](),32,10)
Cookies_model = run_model('Cookies',dfs['Cookies'](),32,5)
Organic_Vegetables_model = run_model('Organic_Vegetables',dfs['Organic_Vegetables'](),32,10)
Flour_model = run_model('Flour',dfs['Flour'](),32,5)
Edible_Oil_model = run_model('Edible_Oil',dfs['Edible_Oil'](),32,10)
Chicken_model = run_model('Chicken',dfs['Chicken'](),32,10)
Biscuits_model = run_model('Biscuits',dfs['Biscuits'](),32,10)
Legumes_model = run_model('Legumes',dfs['Legumes'](),32,10)
Spices_model = run_model('Spices',dfs['Spices'](),32,10)
Fresh_Fruits_model = run_model('Fresh_Fruits',dfs['Fresh_Fruits'](),32,10)
Rice_model = run_model('Rice',dfs['Rice'](),32,10)
Meat_model = run_model('Meat',dfs['Meat'](),32,25)
Organic_Staples_model = run_model('Organic_Staples',dfs['Organic_Staples'](),32,10)
Fresh_Vegetables_model = run_model('Fresh_Vegetables',dfs['Fresh_Vegetables'](),32,25)
Organic_Fruits_model = run_model('Organic_Fruits',dfs['Organic_Fruits'](),32,5)
Soft_Drinks_model = run_model('Soft_Drinks',dfs['Soft_Drinks'](),32,10)
Eggs_model = run_model('Eggs',dfs['Eggs'](),32,5)

import requests
import pandas as pd
url = "https://damang2003.et.r.appspot.com/transaksi"

params = {"user_id": 1}
response = requests.get(url, params=params)
# Menggunakan metode GET untuk mengambil data
response = requests.get(url)

# Memeriksa apakah permintaan berhasil (kode status 200)
if response.status_code == 200:
    # Mendapatkan data dalam format JSON
    data = response.json()
    data = pd.DataFrame(data)
else:
    # Menampilkan pesan kesalahan jika permintaan tidak berhasil
    print(f"Failed to retrieve data. Status code: {response.status_code}")
    print(response.text)

# Sort the DataFrame by 'Order Date'
data.sort_values('createdAt', inplace=True)
data.head

# Selecting specific columns
new_selected_columns = ['createdAt', 'nama_barang', 'total_harga']
#selected_columns = ['Order Date', 'Item', 'Sales']
new_df_selected = data[new_selected_columns]

# Rename columns
column_mapping = {'createdAt': 'Order Date', 'nama_barang': 'Item', 'total_harga': 'Sales'}
new_df_selected.rename(columns=column_mapping, inplace=True)

new_df_selected = new_df_selected.dropna(subset=['Sales'])
numerical_columns = ['Sales']
new_scaler = MinMaxScaler()
new_df_selected[numerical_columns] = new_scaler.fit_transform(new_df_selected[numerical_columns])

#distinc the items
items = new_df_selected['Item'].drop_duplicates(keep = 'first')

# Create a dictionary to store DataFrames for each item
new_dfs = {}
#make a new df for every items
for item in items:
    item_df = pd.DataFrame(new_df_selected.loc[new_df_selected['Item'] == item]).copy
    new_dfs[item] = item_df

def predict_future(model, data, sequence_length):
    # Normalization of data
    sequences, y = create_sequences(data, sequence_length)

    # Take the last 'sequence_length' days as the initial condition
    initial_data = sequences[-sequence_length:]

    # Ensure that the shape matches the model's input requirements
    initial_data = np.expand_dims(initial_data, axis=-1)  # Add an extra dimension if needed

    # Perform prediction for the next day
    next_day_prediction = model.predict(initial_data)

    # Inverse normalization of the prediction
    prediction = next_day_prediction.reshape(-1, 1)

    return prediction

Overall_sales_pred = predict_future(Overall_model,new_df_selected, 20)
Breads_sales_pred = predict_future(Breads_model,new_dfs['Breads'](), 25)
Cakes_sales_pred = predict_future(Cakes_model,new_dfs['Cakes'](),15)
Health_Drinks_sales_pred = predict_future(Health_Drinks_model,new_dfs['Health_Drinks'](),5)
Chocolates_sales_pred = predict_future(Chocolates_model,new_dfs['Chocolates'](),5)
Fish_sales_pred = predict_future(Fish_model,new_dfs['Fish'](),15)
Salt_sales_pred = predict_future(Salt_model,new_dfs['Salt'](),15)
Noodles_sales_pred = predict_future(Noodles_model,new_dfs['Noodles'](),10)
Cookies_sales_pred = predict_future(Cookies_model,new_dfs['Cookies'](),5)
Organic_Vegetables_sales_pred = predict_future(Organic_Vegetables_model,new_dfs['Organic_Vegetables'](),10)
Flour_sales_pred = predict_future(Flour_model,new_dfs['Flour'](),5)
Edible_Oil_sales_pred = predict_future(Edible_Oil_model,new_dfs['Edible_Oil'](),10)
Chicken_sales_pred = predict_future(Chicken_model,new_dfs['Chicken'](),10)
Biscuits_sales_pred = predict_future(Biscuits_model,new_dfs['Biscuits'](),10)
Legumes_sales_pred = predict_future(Legumes_model,new_dfs['Legumes'](),10)
Spices_sales_pred = predict_future(Spices_model,new_dfs['Spices'](),10)
Fresh_Fruits_sales_pred = predict_future(Fresh_Fruits_model,new_dfs['Fresh_Fruits'](),10)
Rice_sales_pred = predict_future(Rice_model,new_dfs['Rice'](),10)
Meat_sales_pred = predict_future(Meat_model,new_dfs['Meat'](),25)
Organic_Staples_sales_pred = predict_future(Organic_Staples_model,new_dfs['Organic_Staples'](),10)
Fresh_Vegetables_sales_pred = predict_future(Fresh_Vegetables_model,new_dfs['Fresh_Vegetables'](),25)
Organic_Fruits_sales_pred = predict_future(Organic_Fruits_model,new_dfs['Organic_Fruits'](),5)
#Soft_Drinks_sales_pred = predict_future(Soft_Drinks_model,new_dfs['Soft_Drinks'](),10)
#Eggs_sales_pred = predict_future(Eggs_model,new_dfs['Eggs'](),5)

Overall_result=new_scaler.inverse_transform(Overall_sales_pred.reshape(-1,1))
Breads_result=new_scaler.inverse_transform(Breads_sales_pred.reshape(-1,1))
Cakes_result=new_scaler.inverse_transform(Cakes_sales_pred.reshape(-1,1))
Health_Drinks_result=new_scaler.inverse_transform(Health_Drinks_sales_pred.reshape(-1,1))
Chocolates_result=new_scaler.inverse_transform(Chocolates_sales_pred.reshape(-1,1))
Fish_result=new_scaler.inverse_transform(Fish_sales_pred.reshape(-1,1))
Salt_result=new_scaler.inverse_transform(Salt_sales_pred.reshape(-1,1))
Noodles_result=new_scaler.inverse_transform(Noodles_sales_pred.reshape(-1,1))
Cookies_result=new_scaler.inverse_transform(Cookies_sales_pred.reshape(-1,1))
Organic_Vegetables_result=new_scaler.inverse_transform(Organic_Vegetables_sales_pred.reshape(-1,1))
Flour_result=new_scaler.inverse_transform(Flour_sales_pred.reshape(-1,1))
Edible_Oil_result=new_scaler.inverse_transform(Edible_Oil_sales_pred.reshape(-1,1))
Chicken_result=new_scaler.inverse_transform(Chicken_sales_pred.reshape(-1,1))
Biscuits_result=new_scaler.inverse_transform(Biscuits_sales_pred.reshape(-1,1))
Legumes_result=new_scaler.inverse_transform(Legumes_sales_pred.reshape(-1,1))
Spices_result=new_scaler.inverse_transform(Spices_sales_pred.reshape(-1,1))
Fresh_Fruits_result=new_scaler.inverse_transform(Fresh_Fruits_sales_pred.reshape(-1,1))
Rice_result=new_scaler.inverse_transform(Rice_sales_pred.reshape(-1,1))
Meat_result=new_scaler.inverse_transform(Meat_sales_pred.reshape(-1,1))
Organic_Staples_result=new_scaler.inverse_transform(Organic_Staples_sales_pred.reshape(-1,1))
Fresh_Vegetables_result=new_scaler.inverse_transform(Fresh_Vegetables_sales_pred.reshape(-1,1))
Organic_Fruits_result=new_scaler.inverse_transform(Organic_Fruits_sales_pred.reshape(-1,1))
#Soft_Drinks_result=new_scaler.inverse_transform(Soft_Drinks_sales_pred.reshape(-1,1))
#Eggs_result=new_scaler.inverse_transform(Eggs_sales_pred.reshape(-1,1))

def trend(name,result, df):
  df_sales = df[['Sales']]  # Extracting the 'Sales' column
  df_sales_inverse = new_scaler.inverse_transform(df_sales)
  if result[-1]>df_sales_inverse[-1]:
    print(f"{name} Trendline is Uptrend")
  else:
    print(f"{name} Trendline is Downtrend")

trend('Overall', Overall_result,new_df_selected)
trend('Breads',Breads_result, new_dfs['Breads']())
trend('Cakes',Cakes_result, new_dfs['Cakes']())
trend('Health_Drinks',Health_Drinks_result, new_dfs['Health_Drinks']())
trend('Chocolates',Chocolates_result, new_dfs['Chocolates']())
trend('Fish',Fish_result, new_dfs['Fish']())
trend('Salt',Salt_result, new_dfs['Salt']())
trend('Noodles',Noodles_result, new_dfs['Noodles']())
trend('Cookies',Cookies_result, new_dfs['Cookies']())
trend('Organic_Vegetables',Organic_Vegetables_result, new_dfs['Organic_Vegetables']())
trend('Flour',Flour_result, new_dfs['Flour']())
trend('Edible_Oil',Edible_Oil_result, new_dfs['Edible_Oil']())
trend('Chicken',Chicken_result, new_dfs['Chicken']())
trend('Biscuits',Biscuits_result, new_dfs['Biscuits']())
trend('Legumes',Legumes_result, new_dfs['Legumes']())
trend('Spices',Spices_result, new_dfs['Spices']())
trend('Fresh_Fruits',Fresh_Fruits_result, new_dfs['Fresh_Fruits']())
trend('Rice',Rice_result, new_dfs['Rice']())
trend('Meat',Meat_result, new_dfs['Meat']())
trend('Organic_Staples',Organic_Staples_result, new_dfs['Organic_Staples']())
trend('Fresh_Vegetables',Fresh_Vegetables_result, new_dfs['Fresh_Vegetables']())
trend('Organic_Fruits',Organic_Fruits_result, new_dfs['Organic_Fruits']())
#trend('Soft_Drinks',Soft_Drinks_result, new_dfs['Soft_Drinks']())
#trend('Eggs',Eggs_result, new_dfs['Eggs']())
