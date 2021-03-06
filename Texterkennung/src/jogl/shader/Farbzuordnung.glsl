#version 430

//in uvec3 gl_NumWorkGroups; // Check how many work groups there are. Provided for convenience.
//in uvec3 gl_WorkGroupID; // Check which work group the thread belongs to.
//in uvec3 gl_LocalInvocationID; // Within the work group, get a unique identifier for the thread.
//in uvec3 gl_GlobalInvocationID; // Globally unique value across the entire compute dispatch. Short-hand for gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
//in uint gl_LocalInvocationIndex; // 1D version of gl_LocalInvocationID. Provided for convenience.

//Buffer input
layout (std430) buffer;

layout (binding = 0) readonly buffer SSBO {
int farbe[];
} farben;

layout (binding = 1) readonly buffer InputData {
int datain[];
} inputData;

layout (binding = 2) writeonly buffer OutputData {
int dataout[];
} outputData;

layout (binding = 3) writeonly buffer OutputDataF {
float dataoutF[];
} outputDataF;


// Set the number of invocations in the work group.
layout (local_size_x = 1024) in;

uniform int schwellwert;

float fColor(int color, int vcolor)
{
	int rot = (color >> 16) & 0xff;
	int gruen = (color >> 8)  & 0xff;
	int blau = (color) & 0xff;
	
	int rot2 = (vcolor >> 16) & 0xff;
	int gruen2 = (vcolor >> 8)  & 0xff;
	int blau2 = (vcolor) & 0xff;
	
	float l = pow(rot - rot2, 2) + pow(gruen - gruen2, 2) + pow(blau - blau2, 2);
	
	return sqrt(l) / schwellwert;
}

void main()
{
	if (gl_GlobalInvocationID.x > inputData.datain.length()) return;
	
	float f = 1.0f;
	outputData.dataout[gl_GlobalInvocationID.x] = -1;
	for (int i = 0; i < farben.farbe.length(); i++)
	{
		float f0 = fColor(farben.farbe[i], inputData.datain[gl_GlobalInvocationID.x]);
		if (f0 < f)
		{
			f = f0;
			outputData.dataout[gl_GlobalInvocationID.x] = i;
		}
	}
	
	outputDataF.dataoutF[gl_GlobalInvocationID.x] = f;
}

