# retrofitclient
스프링부트와 retrofic클라이언트 툴 예제 + application/octet-stream

서버사이드 컨트롤러... :: server side controller

	@PostMapping(value = "/api/liders/tracksinfos", consumes = { "application/octet-stream" } )
	@ResponseStatus(HttpStatus.CREATED)
	public String saveLiDERLocation2Packet( @RequestHeader(required = true) Long liderId, @RequestHeader Long vehicleId, @RequestBody byte[] p) {

		try {
			//압축 해제
			byte[] packet = zLibDecompress( p );

			tracksInfoService.saveTracksInfoPacket(packet, liderId, vehicleId);			

		} catch (DataFormatException e) {			
			String err = "Data format exception on decompress packet...";
			log.error( err );
			throw new CustomException( err );
			
		} catch (IOException e) {
			throw new CustomException( "[IOException] " + e.getLocalizedMessage() );
		}
		
		return HttpStatus.CREATED.toString();
	}
	
	@GetMapping( "/api/liders/tracksinfos" )
	@ResponseStatus(HttpStatus.OK)
	public void temp() {
		log.info("@~~ i'm alive.......");
	}
