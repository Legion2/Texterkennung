/**
 * 
 */
package texterkennung.operator.ImageScaling;

import texterkennung.data.Data_ID;

/**
 * @author Fabio Schmidberger
 *
 */
public class Operator_BilinearImageScaling extends Operator_ImageScaling implements IData2dScalar {

	/**
	 * @param originalImage
	 * @param scaleFaktor
	 */
	public Operator_BilinearImageScaling(Data_ID originalImage, float scaleFaktor) {
		super(originalImage, scaleFaktor);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see texterkennung.operator.ImageScaling.IData2dScalar#scale()
	 */
	@Override
	public Data_ID scale() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see texterkennung.operator.Operator#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
