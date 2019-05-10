appstage=$1
if [ "$1" == "" ]; then
   appstage="PROD"
fi
echo "Starting Applikation on " $appstage

docker run -itd \
-e APP_STAGE=$appstage \
-p 18080:8080 \
-p 18443:8443 \
--name salary \
dockregxwr-on.azurecr.io/seicentosalary