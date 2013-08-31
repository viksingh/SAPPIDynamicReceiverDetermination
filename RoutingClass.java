package com.saki.dynamicreceiver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.xml.sax.SAXException;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

public class RoutingClass extends AbstractTransformation {

	public void transform(TransformationInput input, TransformationOutput output) throws StreamTransformationException {
		try {
			this.execute(input.getInputPayload().getInputStream(), output.getOutputPayload().getOutputStream());
		} catch (SAXException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void execute(InputStream in, OutputStream out) throws StreamTransformationException, SAXException, IOException {
		try {

			String inputPayload = new Scanner(in, "UTF-8").useDelimiter("\\A").next();

			String sapsys = System.getProperty("SAPSYSTEMNAME");
			write_trace("SAP SID is"+sapsys, sapsys);
			
			
			getTrace().addDebugMessage("Debug - SID IS: "+sapsys);
			getTrace().addWarning("Warning - SID IS : "+sapsys);
			getTrace().addInfo("Info - SID IS : "+sapsys);

			

			String XMLDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			String header = "<ns1:Receivers xmlns:ns1=\"http://sap.com/xi/XI/System\">";
			String ReceiverStr = "";

			if (inputPayload.toLowerCase().contains("vikas".toLowerCase())) {
				ReceiverStr = XMLDeclaration + header + "<Receiver>" + "<Service>" + "AOA_AU_S_AUEDUMMY02_SB_RCV" + "</Service>" + "</Receiver>"
						+ "</ns1:Receivers>";								}
			else 															{
				ReceiverStr = XMLDeclaration + header + "<Receiver>" + "<Service>" + "AOA_AU_S_AUEDUMMY01_SB_RCV" + "</Service>" + "</Receiver>"
						+ "</ns1:Receivers>";
																			}

			out.write(ReceiverStr.getBytes());
		} catch (Exception e) {
			throw new StreamTransformationException(e.toString());
		}
	}

	// Write Trace while testing standalone or PI
	private void write_trace(String str, String SID) {
		if (SID == null) {
			System.out.println(str);
		} else {
			getTrace().addInfo(str);
		}
	}

	public static void main(String[] args) {

		try {
			RoutingClass map = new RoutingClass();
			FileInputStream in = new FileInputStream("C:\\PI_TEST\\DynamicRouting\\input\\input_no_receiver.txt");
			FileOutputStream out = new FileOutputStream("C:\\PI_TEST\\DynamicRouting\\input\\out_receiver.txt");
			map.execute(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
