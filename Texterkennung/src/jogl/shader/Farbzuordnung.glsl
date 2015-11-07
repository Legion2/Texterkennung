#version 430

in uvec3 gl_NumWorkGroups; // Check how many work groups there are. Provided for convenience.
in uvec3 gl_WorkGroupID; // Check which work group the thread belongs to.
in uvec3 gl_LocalInvocationID; // Within the work group, get a unique identifier for the thread.
in uvec3 gl_GlobalInvocationID; // Globally unique value across the entire compute dispatch. Short-hand for gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
in uint gl_LocalInvocationIndex; // 1D version of gl_LocalInvocationID. Provided for convenience.

layout (binding=0, rgba8) readonly uniform image2D depthBuffer;
layout (binding=1, rgba8) writeonly uniform image2D colorBuffer;





// Set the number of invocations in the work group.
layout (local_size_x = 16, local_size_y = 16) in;



void main()
{

}