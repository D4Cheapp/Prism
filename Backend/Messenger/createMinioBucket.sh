/usr/bin/mc config host add local http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD};
/usr/bin/mc rm -r --force local/prism-bucket;
/usr/bin/mc mb -p local/prism-bucket;
/usr/bin/mc policy set download local/prism-bucket;
exit 0;
