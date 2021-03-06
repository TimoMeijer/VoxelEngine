#version 150 core

uniform sampler2DRect texture_diffuse;

in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
	// Override out_Color with our texture pixel
	out_Color = texture(texture_diffuse, pass_TextureCoord);
}