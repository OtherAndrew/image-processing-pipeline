#cloud_function(platforms=[Platform.AWS], memory=512, config=config)
def handle(request, context):
    from Inspector import Inspector
    import boto3
    from types_boto3_s3.service_resource import S3ServiceResource
    from PIL import Image
    import io


    # Import the module and collect data 
    inspector = Inspector()
    inspector.inspectAll()


    bucket_name = request['bucketname']
    original_file = request['originalfilename']
    new_file = request['newfilename']

    s3: S3ServiceResource = boto3.resource('s3')
    bucket = s3.Bucket(bucket_name)

    in_stream = io.BytesIO()
    out_stream = io.BytesIO()

    bucket.download_fileobj(original_file, in_stream)

    image = Image.open(in_stream)


    image = image.convert('L')

    image.save(out_stream, "PNG")

    out_stream.seek(0)
    bucket.upload_fileobj(out_stream, new_file, ExtraArgs={'ContentType': 'image/png'})

    inspector.inspectAllDeltas()
    return inspector.finish()
