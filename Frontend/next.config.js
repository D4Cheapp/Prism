/** @type {import('next').NextConfig} */

const nextConfig = {
  reactStrictMode: false,
  webpack: (config, options) => {
    config.module.rules.push(
      {
        test: /\.svg$/i,
        issuer: /\.[jt]sx?$/,
        use: ['@svgr/webpack'],
      },
    );
    return config;
  },
  sassOptions: {
    prependData: `@import "src/app/styles/mixins";`,
  },
  eslint: {
    dirs: ['src'],
  },
  env: {
    AUTH_SERVER_URL: process.env.AUTH_SERVER_URL,
    MESSENGER_SERVER_URL: process.env.MESSENGER_SERVER_URL
  }
};

module.exports = nextConfig;
