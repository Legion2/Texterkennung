package texterkennung.operator.ImageScaling;

import texterkennung.data.Data_ID;

/**
 * 
 * @author Fabio Schmidberger
 *
 *	Interface for Image-Scaling
 *	-> Possibility to implement several more Image-Scaling Operators in the future
 */

public interface IData2dScalar {
	
	public Data_ID scale();

}
