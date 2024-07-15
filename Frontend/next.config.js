/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,
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
