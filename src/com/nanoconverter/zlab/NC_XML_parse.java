package com.nanoconverter.zlab;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class NC_XML_parse extends AsyncTask<Document, Void, Void>  {
	
    protected void onPreExecute() {
      super.onPreExecute();
      /* ������� ������ */
        if (!NC_main.prefs_updates.equals("background")) {
      NC_main.dialog_XML = ProgressDialog.show(NC_main.mainContext, "Parse XML...", "Please wait");
        }
    }
    
	@Override
	protected Void doInBackground(Document... params) {

		/* TODO �������� ID ���������, ����������, ������ */

		/* TODO
		 * if date=null, ���� �� ����� �� �������.
		 * else ���� �� ����� �� ��� ����
		 */

	try {
  		NodeList valute_ID		= null; 	/* ������������� ������, USD, EUR, etc. */
  		NodeList valute_nominal	= null;		/* ������� ������. ������ = (������� * ��������) */
  		NodeList valute_value	= null;		/* �������� ������ */
  		NodeList valute_list 	= null;		/* ��� ECB */
  		/* DONE �������� ������� ��� ������ ������ � ���������� ���������� � ������������� */
		if (NC_main.prefs_source.equals("CBR") || NC_main.prefs_source.equals("NBU")){/* CBR, NBU */
	  		valute_ID		= params[0].getElementsByTagName("CharCode");
	  		valute_nominal	= params[0].getElementsByTagName("Nominal");
	  		valute_value	= params[0].getElementsByTagName("Value");
	  		NC_main.len = valute_ID.getLength()+1;		/* ���������� ����� � �������� ����� */
	  		} else
	  	if (NC_main.prefs_source.equals("ECB")){/* ECB */
			valute_list = params[0].getElementsByTagName("Cube");
			NC_main.len = valute_list.getLength()-1; 	/* ���������� ����� � �������� ����� */
	  		}

		NC_main.valute_ID = new String[NC_main.len];
		NC_main.valute_value = new String[NC_main.len];
		NC_main.valute_name = new String[NC_main.len];

		if (NC_main.prefs_source.equals("CBR") || NC_main.prefs_source.equals("NBU")){/* CBR, NBU */
			for(int i = 0; i<NC_main.len; i++){
				if (i==NC_main.len-1){
					if (NC_main.prefs_source.equals("CBR")){NC_main.valute_ID[i] = "RUB";NC_main.valute_value[i] = "1.0000";} else
					if (NC_main.prefs_source.equals("NBU")){NC_main.valute_ID[i] = "UAH";NC_main.valute_value[i] = "1.0000";}
				} else {
				NC_main.valute_ID[i]	= valute_ID.item(i).getFirstChild().getNodeValue();
				NC_main.valute_value[i] = String.valueOf(Float.parseFloat(valute_value.item(i).getFirstChild().getNodeValue().replace(",", "."))/Float.parseFloat(valute_nominal.item(i).getFirstChild().getNodeValue()));
				}
			}
		}
else	if (NC_main.prefs_source.equals("ECB")){/* ECB */
			for(int i = 0; i<NC_main.len; i++){
			if (i==NC_main.len-1){NC_main.valute_ID[i] = "EUR";NC_main.valute_value[i] = "1.0000";} else {
			   NC_main.valute_ID[i]		= valute_list.item(i+2).getAttributes().getNamedItem("currency").getNodeValue();
			   NC_main.valute_value[i]	= String.valueOf(Float.parseFloat("1.0")/Float.parseFloat(valute_list.item(i+2).getAttributes().getNamedItem("rate").getNodeValue()));
				}
	   		}}
  		 		 /* no parse error */
  			} catch (Exception ioe) {
  		    	 /* parse error */
  				ioe.printStackTrace();
  		    }
		return null;
	}

	@Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      /* DONE �������� ����������� � ������ �� ������ ������� */
      /* DONE ������� ���������� �� main ������
       */
      NC_main.update_VAR();
      NC_main.update_P1(NC_main.mainContext);
      NC_main.update_P2();
        if (!NC_main.prefs_updates.equals("background")) {
      NC_main.dialog_XML.dismiss();                       }
    }
}
