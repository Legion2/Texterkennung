#version 430
#include <iostream>
#include <assert.h>

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

double cubicInterpolate (double p[4], double x) {
	return p[1] + 0.5 * x*(p[2] - p[0] + x*(2.0*p[0] - 5.0*p[1] + 4.0*p[2] - p[3] + x*(3.0*(p[1] - p[2]) + p[3] - p[0])));
}

double bicubicInterpolate (double p[4][4], double x, double y) {
	double arr[4];
	arr[0] = cubicInterpolate(p[0], y);
	arr[1] = cubicInterpolate(p[1], y);
	arr[2] = cubicInterpolate(p[2], y);
	arr[3] = cubicInterpolate(p[3], y);
	return cubicInterpolate(arr, x);
}

double tricubicInterpolate (double p[4][4][4], double x, double y, double z) {
	double arr[4];
	arr[0] = bicubicInterpolate(p[0], y, z);
	arr[1] = bicubicInterpolate(p[1], y, z);
	arr[2] = bicubicInterpolate(p[2], y, z);
	arr[3] = bicubicInterpolate(p[3], y, z);
	return cubicInterpolate(arr, x);
}

double nCubicInterpolate (int n, double* p, double coordinates[]) {
	assert(n > 0);
	if (n == 1) {
		return cubicInterpolate(p, *coordinates);
	}
	else {
		double arr[4];
		int skip = 1 << (n - 1) * 2;
		arr[0] = nCubicInterpolate(n - 1, p, coordinates + 1);
		arr[1] = nCubicInterpolate(n - 1, p + skip, coordinates + 1);
		arr[2] = nCubicInterpolate(n - 1, p + 2*skip, coordinates + 1);
		arr[3] = nCubicInterpolate(n - 1, p + 3*skip, coordinates + 1);
		return cubicInterpolate(arr, *coordinates);
	}
}

float fColor(int color, int vcolor)
{
	int rot = (color >> 16) & 0xff;
	int gruen = (color >> 8)  & 0xff;
	int blau = (color) & 0xff;
	
	int rot2 = (vcolor >> 16) & 0xff;
	int gruen2 = (vcolor >> 8)  & 0xff;
	int blau2 = (vcolor) & 0xff;
	
	float l = pow(rot - rot2, 2) + pow(gruen - gruen2, 2) + pow(blau - blau2, 2);
	
	return l / pow(schwellwert, 2);
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

