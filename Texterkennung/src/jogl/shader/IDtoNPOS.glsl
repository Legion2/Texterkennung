#version 430
#define treadcount 1024

//in uvec3 gl_NumWorkGroups; // Check how many work groups there are. Provided for convenience.
//in uvec3 gl_WorkGroupID; // Check which work group the thread belongs to.
//in uvec3 gl_LocalInvocationID; // Within the work group, get a unique identifier for the thread.
//in uvec3 gl_GlobalInvocationID; // Globally unique value across the entire compute dispatch. Short-hand for gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
//in uint gl_LocalInvocationIndex; // 1D version of gl_LocalInvocationID. Provided for convenience.

//Buffer input
layout (std430) buffer;

layout (binding = 0) readonly buffer InputData {
int datain[];
} inputData;

layout (binding = 1) writeonly buffer OutputData {
int dataout[];
} outputData;

shared int shareData[treadcount];

uniform int offset;

// Set the number of invocations in the work group.
layout (local_size_x = treadcount) in;

void synchronize()
{
	// Ensure that memory accesses to shared variables complete.
	memoryBarrierShared();
	// Every thread in work group must reach this barrier before any other thread can continue.
	barrier();
}

void main()
{
	int count = (inputData.datain.length() / treadcount) + 1;
	
	int lastindex = -1;
	
	int ID = int(gl_LocalInvocationID.x);
	int vergleich = int(gl_WorkGroupID.x + offset);
	
	for (int i = ID * count; i < ID * count + count && i < inputData.datain.length(); i++)
	{
		if (inputData.datain[i] == vergleich)
		{
			if (lastindex == -1)
			{
				lastindex = i;
				shareData[gl_LocalInvocationID.x] = i;
			}
			else
			{
				outputData.dataout[lastindex] = i;
				lastindex = i;
			}
		}
			
	}
	if (lastindex != -1)
	{
		synchronize();
		for (int j = ID + 1; j < ID + treadcount; j++)
		{
			if (shareData[j % treadcount] != -1)
			{
				outputData.dataout[lastindex] = shareData[j % treadcount];
				return;
			}
		}
	}
	else
	{
		shareData[gl_LocalInvocationID.x] = -1;
		synchronize();
	}
}

