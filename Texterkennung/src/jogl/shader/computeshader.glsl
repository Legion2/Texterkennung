#version 430

in uvec3 gl_NumWorkGroups; // Check how many work groups there are. Provided for convenience.
in uvec3 gl_WorkGroupID; // Check which work group the thread belongs to.
in uvec3 gl_LocalInvocationID; // Within the work group, get a unique identifier for the thread.
in uvec3 gl_GlobalInvocationID; // Globally unique value across the entire compute dispatch. Short-hand for gl_WorkGroupID * gl_WorkGroupSize + gl_LocalInvocationID;
in uint gl_LocalInvocationIndex; // 1D version of gl_LocalInvocationID. Provided for convenience.

// Set the number of invocations in the work group.
layout (local_size_x = 1024) in;

// Declare the texture inputs
uniform readonly image2D fromTex;
uniform writeonly image2D toTex;



void main()
{
  // Acquire the coordinates to the texel we are to process.
  ivec2 texelCoords = ivec2(gl_GlobalInvocationID.xy);
 
  // Read the pixel from the first texture.
  vec4 pixel = imageLoad(fromTex, texelCoords);
 
  // Swap the red and green channels.
  pixel.rg = pixel.gr;
 
  // Now write the modified pixel to the second texture.
  imageStore(toTex, texelCoords, pixel);
}