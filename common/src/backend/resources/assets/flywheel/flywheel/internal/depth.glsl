// Minecraft 26.3 renders with reversed depth: the near plane maps to 1 and the far plane to 0.
// With either clip-control convention ([0, 1] or [-1, 1] NDC) the window depth is
//     d = zNear * (zFar - e) / ((zFar - zNear) * e)
// where e is the positive eye-space distance.

float linearize_depth(float d, float zNear, float zFar) {
    return zNear * zFar / (d * (zFar - zNear) + zNear);
}

float delinearize_depth(float linearDepth, float zNear, float zFar) {
    return zNear * (zFar - linearDepth) / ((zFar - zNear) * linearDepth);
}
