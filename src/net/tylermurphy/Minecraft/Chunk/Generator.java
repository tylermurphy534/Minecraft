package net.tylermurphy.Minecraft.Chunk;

public class Generator {

	private static int OCTAVES = 2;
	private static int AMPLITUDE = 10;
	private static int ROUGHNESS = 3;
	
	private static int generateHeight(int x, int z) {
        float total = 0;
        float d = (float) Math.pow(2, OCTAVES-1);
        for(int i=0;i<OCTAVES;i++){
            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += getInterpolatedNoise((x)*freq, (z)*freq) * amp;
        }
        return (int) total+100;
    }
	
	private static float interpolate(float a, float b, float blend){
        double theta = blend * Math.PI;
        float f = (float)(1f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }
	
	private static float getInterpolatedNoise(float x, float z){
        int intX = (int) x;
        int intZ = (int) z;
        float fracX = x - intX;
        float fracZ = z - intZ;
         
        float v1 = (float) PerlinNoise.getNoise(Chunk.SEED, intX, intZ);
        float v2 = (float) PerlinNoise.getNoise(Chunk.SEED, intX + 1, intZ);
        float v3 = (float) PerlinNoise.getNoise(Chunk.SEED, intX, intZ + 1);
        float v4 = (float) PerlinNoise.getNoise(Chunk.SEED, intX + 1, intZ + 1);
        float i1 = interpolate(v1, v2, fracX);
        float i2 = interpolate(v3, v4, fracX);
        return interpolate(i1, i2, fracZ);
    }
	
	private static void generateLandscape(Chunk c) {
		for(int x=0;x<16;x++) {
			for(int z=0;z<16;z++) {
				int top = generateHeight(c.gridX*16+x, c.gridZ*16+z);
				for(int y=0;y<256;y++) {
					if(top<=95) {
						if(y < top) c.cubes[x][y][z] = 14;
						else if(y == top) c.cubes[x][y][z] = 13;
						else if(y <= 90) c.cubes[x][y][z] = 17;
						else c.cubes[x][y][z] = Cube.AIR;
					} else {
						if(y < top - 90) c.cubes[x][y][z] = 2;
						else if(y < top) c.cubes[x][y][z] = 1;
						else if(y == top) c.cubes[x][y][z] = 0;
						else c.cubes[x][y][z] = Cube.AIR;
					}
				}
			}
		}
	}
	
	private static void generateNature(Chunk c) {
		for(int x=0;x<16;x++) {
			for(int z=0;z<16;z++) {
				int top = generateHeight(c.gridX*16+x, c.gridZ*16+z);
				double n1 = PerlinNoise.getNoise(c.chunk_seed, x*2+z, z/2-x,PerlinNoise.NoiseType.WhiteNoise);
				double n2 = PerlinNoise.getNoise(c.chunk_seed, z*5-z*2, x/4+6-x*5,PerlinNoise.NoiseType.WhiteNoise);
				double n3 = PerlinNoise.getNoise(c.chunk_seed, x/5+17, z/14,PerlinNoise.NoiseType.WhiteNoise);
				double n4 = PerlinNoise.getNoise(c.chunk_seed, z+5, (int)Math.pow(x,2),PerlinNoise.NoiseType.WhiteNoise);
				if(top<=95) {
					if(n1*n2/n3*n4>2 && top > 90) {
						c.cubes[x][top+1][z] = 15;
						c.cubes[x][top+2][z] = 15;
						c.cubes[x][top+3][z] = 15;
					}
				} else {
					if(n1*n2/n4*n3>2 && top > 90) {
						if(x < 1 || x > 14 || z < 1 || z > 14) continue;
						c.cubes[x][top+1][z] = 10;
						c.cubes[x][top+2][z] = 10;
						c.cubes[x][top+3][z] = 10;
						
						c.cubes[x-1][top+3][z] = 12;
						c.cubes[x-1][top+3][z+1] = 12;
						c.cubes[x][top+3][z+1] = 12;
						c.cubes[x+1][top+3][z+1] = 12;
						c.cubes[x+1][top+3][z] = 12;
						c.cubes[x+1][top+3][z-1] = 12;
						c.cubes[x][top+3][z-1] = 12;
						c.cubes[x-1][top+3][z-1] = 12;
						
						c.cubes[x+1][top+4][z] = 12;
						c.cubes[x-1][top+4][z] = 12;
						c.cubes[x][top+4][z+1] = 12;
						c.cubes[x][top+4][z-1] = 12;
						c.cubes[x][top+4][z] = 12;
					}
				}
			}
		}
	}
	
	public static void generate(Chunk c) {
		generateLandscape(c);
		generateNature(c);
	}
	
}