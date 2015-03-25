package com.nanoconverter.zlab;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class NC_XML_get extends AsyncTask<String, Void, Document> {

	    protected void onPreExecute() {
	      super.onPreExecute();
	      /* ??????? ?????? */

            if (!NC_main.prefs_updates.equals("background")) {
	      NC_main.dialog_XML = ProgressDialog.show(NC_main.mainContext, "Loading XML...", "Please wait");
            }
	    }

		@Override
		protected Document doInBackground(String... params) {

            /* TODO Если нет сети, исправить падение */
			// URI
			Document doc = null; /* ?????? ??? ?????? XML */

			File root = android.os.Environment.getExternalStorageDirectory(); /* ???? ?? SD */
			File dir  = new File (root.getAbsolutePath() + "/nanoConverter/xmls"); /* ???? ?? ???????? ? XML */
			File file = new File(dir, params[0]+"_"+params[1]+".xml"); /* ???? ?? XML, param[0] - bank_id, param[2] - date */

			String bank_XML_URI=null;
			if (NC_main.prefs_source.equals("CBR")){
				bank_XML_URI = "http://www.cbr.ru/scripts/XML_daily.asp";} else
			if (NC_main.prefs_source.equals("NBU")){
				bank_XML_URI = "http://pfsoft.com.ua/service/currency/";} else
			if (NC_main.prefs_source.equals("ECB")){
				bank_XML_URI = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";}
			
			/* DONE ???????? ??? ????????? URI ?? bank_ID
			 */

			try {
			  	// URI			  	
	            if(!dir.exists()) {dir.mkdirs();}
	            if(file.exists()) {file.delete();}
	            /* TODO ??????? ????????? ????? ???????? XML ? ?????????? ?? ????, ? ?????? ?????? ??? ???????? */
			  	URL  url  = new URL(bank_XML_URI);

	            /* ???????? XML */
	            URLConnection ucon = url.openConnection();
	            InputStream is = ucon.getInputStream();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
	            int current = 0;
	            while ((current = bis.read()) != -1) {
	               baf.append((byte) current);
	            }

	            /* ?????????? ?? ????? ?????? */
	            FileOutputStream fos = new FileOutputStream(file);
	            fos.write(baf.toByteArray());
	            fos.flush();
	            fos.close();
	            
	            /* ????????? ???????? ? ????? ?????? ? ?????? */
			    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  	     	DocumentBuilder db = dbf.newDocumentBuilder();
	  	        doc = db.parse(new InputSource(new InputStreamReader(new FileInputStream(file))));

	            	/* download success */
			  	} catch (Exception e) {
			  		/* download error */
			  	}

			return doc;
		}

	    protected void onPostExecute(Document result) {
	      super.onPostExecute(result);
	      /* ??????? ?????? */
	      NC_main.doc = result;
            if (!NC_main.prefs_updates.equals("background")) {
	      NC_main.dialog_XML.dismiss();                       } else {
                Toast toast = Toast.makeText(NC_main.mainContext, R.string.UI_update_done, Toast.LENGTH_SHORT);
                LinearLayout ToastView = (LinearLayout) toast.getView();
                ImageView imageWorld = new ImageView(NC_main.mainContext);
                imageWorld.setImageResource(R.drawable.ui_icon_good);
                ToastView.addView(imageWorld, 0);
                toast.show();
            }
	      new NC_XML_parse().execute(result);
	    }

}
