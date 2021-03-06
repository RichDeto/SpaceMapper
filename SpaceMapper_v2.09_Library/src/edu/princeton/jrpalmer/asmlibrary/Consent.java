/*
 * Space Mapper
 * Copyright (C) 2012, 2013 John R.B. Palmer
 * Contact: jrpalmer@princeton.edu
 * 
 * This file is part of Space Mapper.
 * 
 * Space Mapper is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at 
 * your option) any later version.
 * 
 * Space Mapper is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with Space Mapper.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.princeton.jrpalmer.asmlibrary;

import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays the IRB consent form and allows users to consent or decline.
 * 
 * @author John R.B. Palmer
 * 
 */
public class Consent extends Activity {

	Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.consent);
		context = getApplicationContext();
		PropertyHolder.init(context);

		TextView consent = (TextView) findViewById(R.id.consenttext);
		consent.setText(Html.fromHtml(getString(R.string.consent_text)));
		consent.setTextColor(Color.WHITE);
		consent.setTextSize(getResources()
				.getDimension(R.dimen.textsize_normal));

		final Button consentButton = (Button) findViewById(R.id.consent_button);
		consentButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Date now = new Date(System.currentTimeMillis());
				String consentTime = Util.userDate(now);
				PropertyHolder.setConsentTime(consentTime);
				PropertyHolder.setConsent(true);

				if (PropertyHolder.isRegistered()) {
					if (PropertyHolder.getUserId() != null) {
						ContentResolver ucr = getContentResolver();
						ucr.insert(Util.getUploadQueueUri(context), UploadContentValues
								.createUpload("CON", consentTime));

					}
					Intent i = new Intent(Consent.this, MapMyData.class);
					startActivity(i);
					finish();
					return;
				} else {

					Intent i = new Intent(Consent.this, Registration.class);
					// start the intent
					startActivity(i);
					finish();
					return;
				}
			}
		});

		final Button consentDeclineButton = (Button) findViewById(R.id.consent_decline_button);
		consentDeclineButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (PropertyHolder.getProVersion()) {
					
					PropertyHolder.setShareData(false);
					
					Intent i = new Intent(Consent.this, MapMyData.class);
					startActivity(i);


				} 
				finish();
				return;
			}
		});

	}

}
