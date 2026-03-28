from pandas import *
from matplotlib.pyplot import *

#carichiamo log e vediamo se un ip ha lanciato un botto di richeiste

#CSV con ip e timestamp
#def load_logs(Path to log):

#pd.read_csv() legge csv pandas e converte in data frame pandas (tipo tabelle sql)

#parse_dates=['timestamp']  #parametro per convertire in datetime
#return pd.read_csv(Path to log,parse_dates=['timestamp'])

#richieste da ip (almeno 100 nel giro di 1h)
#def detect_scraping(dataframedelLog,threshold=100,time_window='1H')

#raggruppo e conto gli ip
#counts = log_df.groupby([pd.Grouper(key='timestamp',freq=time_window),'ip']).size()
#suspicious=counts[counts>threshold].reset_index(name='count')

#return suspicious #ip che hanno fatto più di threshold richieste

